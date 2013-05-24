package com.jetllama.rimatyo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener{

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

    }


    public void onSignInSucceeded() {

    }
}
