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
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.example.games.basegameutils.BaseGameActivity;
import com.jetllama.Rimatyo.R;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends BaseGameActivity implements View.OnClickListener {

    //Debug stag
    public final static String TAG = "RIMATYODEBUG";

    //Request code for UI to show with startActivityForResult
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_WAITING_ROOM = 10002;

    private boolean signedIn = false;
    private int currentScreen = -1;

    //All the buttons we need to attach a listener to
    final static int[] CLICKABLES = {
            R.id.GoogleSignInButton,
            R.id.GoogleSignOutButton
    };

    //All the screens we have
    final static int[] SCREENS= {
            R.id.signin_screen,
            R.id.main_menu_screen
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    //Go to the main screen when the app is opened (or login if not logged in)
    @Override
    protected void onStart() {
        switchToScreen(R.id.main_menu_screen);
        super.onStart();
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
}