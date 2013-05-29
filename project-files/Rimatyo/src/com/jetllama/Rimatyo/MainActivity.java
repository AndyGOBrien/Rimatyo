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
import android.widget.Toast;

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

    //Request code for UI to show with startActivityForResult
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_PLAYER_LOBBY = 10002;

    // request codes we use when invoking an external activity
    final int RC_RESOLVE = 5000, RC_UNUSED = 5001;

    private boolean signedIn = false;
    private int currentScreen = -1;



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
            R.id.waiting_screen
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int id: CLICKABLES)
            findViewById(id).setOnClickListener(this);

        setSignInMessages(getString(R.string.signing_in), getString(R.string.signing_out));
    }


    @Override
    protected void onResume() {
        super.onResume();
        signedIn = isSignedIn();
    }

    public void onStop() {
        allowScreenSleep(true);

        super.onStop();
    }

    @Override
    protected void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);

        switch(request){

            case RC_PLAYER_LOBBY:
                if(response == GamesActivityResultCodes.RESULT_LEFT_ROOM || response == Activity.RESULT_CANCELED)
                    leaveRoom();

        }
    }

    private void leaveRoom() {
        allowScreenSleep(true);
        Log.d(TAG, "Leaving room.");
        switchToScreen(R.id.main_menu_screen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Go to the main screen when the app is opened (or login if not logged in)
    @Override
    protected void onStart() {
        signedIn = isSignedIn();
        switchToScreen(R.id.main_menu_screen);
        super.onStart();
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
        if (statusCode != GamesClient.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }

        showWaitingRoom(room);

    }


    //Called when room has been created
    public void onRoomCreated(int statusCode, Room room) {
        Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");

        if (statusCode != GamesClient.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
            showGameError();
            return;
        }

        showWaitingRoom(room);
    }

    //Called when room is fully connected
    public void onRoomConnected(int statusCode, Room room) {
        Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");

        if (statusCode != GamesClient.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }
    }

    @Override
    public void onLeftRoom(int statusCode, String s) {
        Log.d(TAG, "onLeftRoom, code " + statusCode);

        switchToScreen(R.id.main_menu_screen);

    }

    void showGameError() {
        showAlert("Error", "Error Test");
        switchToScreen(R.id.main_menu_screen);
    }

    //-----OnClick Listeners
    public void onClick(View v){
        switch(v.getId()){
            case R.id.GoogleSignInButton:
                beginUserInitiatedSignIn();
                break;

            case R.id.GoogleSignOutButton:
                signOut();
                switchToScreen(R.id.signin_screen);
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
        }

    }


    public void switchToScreen(int newScreenID){

        //Is the player signed in? If not, take him to login screen
        if (!signedIn)
            newScreenID = R.id.signin_screen;


        //Hide all the screens except for the one we want
        currentScreen = newScreenID;
        for(int id : SCREENS)
            findViewById(id).setVisibility(currentScreen == id ? View.VISIBLE : View.GONE);

    }


    public void onSignInFailed() {
        Log.d(TAG, "Sign-in failed.");
        Toast.makeText(getApplicationContext(), "Unable to sign in...", 300);
    }


    public void onSignInSucceeded() {
        Log.d(TAG, "Successfully signed in");

        signedIn = true;
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
            showAlert(getString(R.string.leaderboards_not_available));
        }

    }

    public void startQuickGame(){

        allowScreenSleep(false);

        //Build the room
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 3, 0);

        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);

        switchToScreen(R.id.waiting_screen);
        getGamesClient().createRoom(rtmConfigBuilder.build());


    }

    //Sets the flag to prevent the screen from sleeping. Useful for things like waiting room
    public void allowScreenSleep(boolean allowSleep){
        if(allowSleep)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private RoomConfig.Builder makeBasicRoomConfigBuilder() {
        return RoomConfig.builder(this).setMessageReceivedListener(this).setRoomStatusUpdateListener(this);
    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {

    }

    // We treat most of the room update callbacks in the same way: we update our list of
    // participants and update the display. In a real game we would also have to check if that
    // change requires some action like removing the corresponding player avatar from the screen,
    // etc.
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
    public void onDisconnectedFromRoom(Room room) {

    }

    @Override
    public void onConnectedToRoom(Room room) {

    }

    public void updateRoom(Room room){

    }

    public void showWaitingRoom(Room room){
        int MIN_PLAYERS = 2;
        Intent i = getGamesClient().getRealTimeWaitingRoomIntent(room, MIN_PLAYERS);
        startActivityForResult(i, RC_PLAYER_LOBBY);
    }
}