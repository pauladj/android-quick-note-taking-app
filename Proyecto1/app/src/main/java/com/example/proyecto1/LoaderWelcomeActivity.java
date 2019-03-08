package com.example.proyecto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.MyDB;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LoaderWelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loader_welcome);

        Thread welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(1500);  //Delay of 2 seconds
                } catch (Exception e) {
                    // do nothing
                } finally {
                    // After 2 seconds
                    // check if a user is already logged in
                    MyDB gestorDB = new MyDB(getApplicationContext(), "Notes", null, 1);
                    String loggedInUsername = gestorDB.getActiveUsername();
                    if (loggedInUsername != null){
                        // user has previously logged in
                        // set the active username so we don't have to log in every time
                        Data.getMyData().setActiveUsername(loggedInUsername);

                        // go to main activity
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        // user has yet to log in
                        logIn();
                    }
                }
            }
        };
        welcomeThread.start();
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

}
