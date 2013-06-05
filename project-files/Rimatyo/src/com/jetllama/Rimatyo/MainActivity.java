/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jetllama.Rimatyo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.BaseGameActivity;

import java.util.List;

public class MainActivity extends BaseGameActivity implements View.OnClickListener, RoomUpdateListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener {

    //Debug stag
    public final static String TAG = "RIMATYODEBUG";
    public final static int MIN_PLAYERS = 3; // 3 + original player

    //Request code for UI to show with startActivityForResult
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_PLAYER_LOBBY = 10002;

    // request codes we use when invoking an external activity whose result doesn't matter
    final int RC_RESOLVE = 5000, RC_UNUSED = 5001;

    private boolean signedIn = false;
    private int currentScreen = -1;
    private boolean animationHasPlayed;





    //All the buttons we need to attach a listener to
    final static int[] CLICKABLES = {
            R.id.GoogleSignInButton,
            R.id.GoogleSignOutButton,
            R.id.quickPlayButton,
            R.id.formTeamButton,
            R.id.rankedMatchButton,
            R.id.achievementButton,
            R.id.leaderboardButton
    };

    //All the screens we have
    final static int[] SCREENS= {
            R.id.signin_screen,
            R.id.main_menu_screen,
            R.id.waiting_screen,
            R.id.empty_screen,
            R.id.test_game_screen
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int id: CLICKABLES)
            findViewById(id).setOnClickListener(this);

        setSignInMessages(getString(R.string.signing_in), getString(R.string.signing_out));

        animationHasPlayed = false;




    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    //Called when app resumes (right after onCreate if first time)
    protected void onResume() {
        super.onResume();
        signedIn = isSignedIn();

        if (!animationHasPlayed){

            ImageButton quickStartButton = (ImageButton)findViewById(R.id.quickPlayButton);
            ImageButton teamButton = (ImageButton)findViewById(R.id.formTeamButton);
            ImageButton rankedButton = (ImageButton)findViewById(R.id.rankedMatchButton);
            ImageButton leaderButton = (ImageButton)findViewById(R.id.leaderboardButton);
            ImageButton achievementButton = (ImageButton)findViewById(R.id.achievementButton);
            TextView rimatyoText = (TextView)findViewById(R.id.titleTextView2);
            ImageButton googleSignoutButton = (ImageButton)findViewById(R.id.GoogleSignOutButton);


            Animation quickPlayAnim = AnimationUtils.loadAnimation(this, R.anim.quickplay_anim);
            Animation teamAnim = AnimationUtils.loadAnimation(this, R.anim.team_button_anim);
            Animation rankedAnim = AnimationUtils.loadAnimation(this, R.anim.ranked_button_anim);
            Animation leaderAnim = AnimationUtils.loadAnimation(this, R.anim.leader_button_anim);
            Animation achievementAnim = AnimationUtils.loadAnimation(this, R.anim.achievement_button_anim);
            Animation rimatyoTextAnim = AnimationUtils.loadAnimation(this, R.anim.rimatyo_text_anim);
            Animation googleSignoutButtonAnim = AnimationUtils.loadAnimation(this, R.anim.google_signout_button_anim);

            quickStartButton.startAnimation(quickPlayAnim);
            teamButton.startAnimation(teamAnim);
            rankedButton.startAnimation(rankedAnim);
            leaderButton.startAnimation(leaderAnim);
            achievementButton.startAnimation(achievementAnim);
            rimatyoText.startAnimation(rimatyoTextAnim);
            googleSignoutButton.startAnimation(googleSignoutButtonAnim);

            animationHasPlayed = true;
        }
    }

    public void onStop() {
        allowScreenSleep(true);
        super.onStop();
    }

    //Go to the main screen when the app is opened (or login if not logged in)
    protected void onStart() {
        signedIn = isSignedIn();
        switchToScreen(R.id.main_menu_screen);
        super.onStart();
    }


    protected void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);

        switch(request){
            case RC_PLAYER_LOBBY:
                if(response == GamesActivityResultCodes.RESULT_LEFT_ROOM || response == Activity.RESULT_CANCELED)
                    leaveRoom();

        }
    }

    /**
     * Click listeners for all the UI elements in the menu screen
     * @param v
     */
    public void onClick(View v){
        switch(v.getId()){
            case R.id.GoogleSignInButton:
                switchToScreen(R.id.empty_screen);
                beginUserInitiatedSignIn();
                break;

            case R.id.GoogleSignOutButton:
                signOut();
                switchToScreen(R.id.signin_screen);
                animationHasPlayed = false;
                break;

            case R.id.achievementButton:
                showAchievements();
                break;

            case R.id.leaderboardButton:
                showLeaderboards();
                break;

            case R.id.quickPlayButton:
                startQuickGame();
                break;

            case R.id.formTeamButton:
                //Use Team game to test the game code
                Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
                MainActivity.this.startActivity(gameIntent);
                break;

            case R.id.testButton:
                switchToScreen(R.id.test_game_screen);
                break;

            case R.id.mainMenuButton:
                switchToScreen(R.id.main_menu_screen);
                break;
        }

    }


    /**
     * Change from one screen to another
     * @param newScreenID
     */
    public void switchToScreen(int newScreenID){

        //Is the player signed in? If not, take him to login screen
        if (!signedIn && newScreenID != R.id.empty_screen)
            newScreenID = R.id.signin_screen;

        //Hide all the screens except for the one we want
        currentScreen = newScreenID;
        for(int id : SCREENS)
            findViewById(id).setVisibility(View.GONE);

        for(int id : SCREENS)
            findViewById(id).setVisibility(currentScreen == id ? View.VISIBLE : View.GONE);

    }


    public void onSignInFailed() {
        Log.e(TAG, "Sign-in failed.");
    }


    public void onSignInSucceeded() {
        Log.d(TAG, "Successfully signed in");

        signedIn = true;
        switchToScreen(R.id.main_menu_screen);
    }


    /**
     * Called when joining an existing game room
     * @param statusCode
     * @param room
     */
    public void onJoinedRoom(int statusCode, Room room) {
        Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
        if (statusCode != GamesClient.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }

        showWaitingRoom(room);

    }

    /**
     * Leave the game room and return to the lobby.
     *
     * TODO: Properly disconnect from a room instead of just changing screens
     */
    private void leaveRoom() {
        allowScreenSleep(true);
        Log.d(TAG, "Leaving room.");
        switchToScreen(R.id.main_menu_screen);
    }


    /**
     * Called when room has been initially created
     * @param statusCode
     * @param room
     */
    public void onRoomCreated(int statusCode, Room room) {
        Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");

        if (statusCode != GamesClient.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
            showGameError();
            return;
        }

        showWaitingRoom(room);
    }

    /**
     * Called when room is fully connected
     *
     * @param statusCode
     * @param room
     */
    public void onRoomConnected(int statusCode, Room room) {
        Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");

        if (statusCode != GamesClient.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }
    }

    /**
     * Called when voluntarily leaving the room
     * @param statusCode
     * @param s
     */
    public void onLeftRoom(int statusCode, String s) {
        Log.d(TAG, "onLeftRoom, code " + statusCode);

        switchToScreen(R.id.main_menu_screen);
    }

    /**
     * Called when disconnected from the room
     * @param room
     */
    public void onDisconnectedFromRoom(Room room) {
        showGameError();

    }


    /**
     * Start a quick game with strangers
     */
    public void startQuickGame(){

        allowScreenSleep(false);

        //Build the room
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_PLAYERS, MIN_PLAYERS, 0);

        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);

        switchToScreen(R.id.waiting_screen);
        getGamesClient().createRoom(rtmConfigBuilder.build());

    }

    /**
     * Sets the flag to prevent the screen from sleeping. Sleeping in certain spots interrupts communication
     * @param allowSleep
     */
    public void allowScreenSleep(boolean allowSleep){
        if(allowSleep)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    // We treat most of the room update callbacks in the same way: we update our list of
    // participants and update the display. In a real game we would also have to check if that
    // change requires some action like removing the corresponding player avatar from the screen,
    // etc.

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {

    }
    @Override
    public void onPeerDeclined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onPeerJoined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onPeerLeft(Room room, List<String> peersWhoLeft) {
        updateRoom(room);
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        updateRoom(room);
    }

    @Override
    public void onRoomConnecting(Room room) {
        updateRoom(room);
    }

    @Override
    public void onPeersConnected(Room room, List<String> peers) {
        updateRoom(room);
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> peers) {
        updateRoom(room);
    }



    @Override
    public void onConnectedToRoom(Room room) {

    }

    public void updateRoom(Room room){

    }

    /**
     * Show the waiting room lobby
     * @param room
     */
    public void showWaitingRoom(Room room){
        Intent i = getGamesClient().getRealTimeWaitingRoomIntent(room, MIN_PLAYERS);
        startActivityForResult(i, RC_PLAYER_LOBBY);
    }

    /**
     * Display an error message and return to the main screen
     */
    void showGameError() {
        showAlert("Error", "Error Test");
        switchToScreen(R.id.main_menu_screen);
    }


    public void showAchievements() {
        if (isSignedIn()) {
            startActivityForResult(getGamesClient().getAchievementsIntent(), RC_UNUSED);
        } else {
            showAlert(getString(R.string.achievements_not_available));
        }
    }

    public void showLeaderboards(){
        if (isSignedIn()) {
            startActivityForResult(getGamesClient().getAllLeaderboardsIntent(), RC_UNUSED);
        } else {
        }

    }
}