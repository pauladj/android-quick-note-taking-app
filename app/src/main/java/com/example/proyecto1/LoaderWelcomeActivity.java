package com.example.proyecto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.proyecto1.utilities.ActiveUser;
import com.example.proyecto1.utilities.MyDB;

import java.io.BufferedReader;
import java.io.IOException;
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
                    // check if internal file exists saying the user is logged in
                    BufferedReader ficherointerno = null;
                    try {
                        ficherointerno = new BufferedReader(new InputStreamReader(
                                openFileInput("activeUser")));
                        String username = ficherointerno.readLine();
                        MyDB gestorDB = new MyDB(getApplicationContext(), "Notes", null, 1);
                        Boolean usernameExists = gestorDB.checkIfUsernameExists(username.trim());
                        if (usernameExists) {
                            // user has previously logged in
                            // set the active username so we don't have to read it from the file every time
                            ActiveUser.getMyActiveUsername().setUsername(username);
                            // go to main activity
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            // user has yet to log in
                            logIn();
                        }
                    }catch (Exception e) {
                        // couldn't read it
                        logIn();
                    }finally {
                        try {
                            ficherointerno.close();
                        }catch (Exception a){
                            // do nothing
                        }
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
