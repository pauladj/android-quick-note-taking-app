package com.example.proyecto1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.MyDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;

public class LoaderWelcomeActivity extends Common {

    private boolean shownAlready = false; // if we change the orientation while
                                            // the thread is running, it will duplicate the
                                            // activity MainClass


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("shownAlready", shownAlready);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey("shownAlready")){
                shownAlready = savedInstanceState.getBoolean("shownAlready");
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loader_welcome);


        Thread welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(1500);  //Delay of 1.5 seconds
                } catch (Exception e) {
                    // do nothing
                } finally {
                    // After 1.5 seconds
                    // check if a user is already logged in
                    String loggedInUsername = getActiveUsername();
                    if (loggedInUsername != null){
                        // user has previously logged in
                        // set the active username so we don't have to log in every time
                        Data.getMyData().setActiveUsername(loggedInUsername);

                        // go to main activity
                        Intent i = new Intent(LoaderWelcomeActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        // user has yet to log in or error in database
                        logIn();
                    }
                }
            }
        };
        if (shownAlready == false){
            welcomeThread.start();
        }

    }

    /**
     * Go to log in screen
     */
    private void logIn(){
        // start login screen
        Intent i = new Intent(this, LogInActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        shownAlready = true; // if the orientation changes while being on the thread
                              // mark it and if so, don't start again the MainActivity
    }
}
