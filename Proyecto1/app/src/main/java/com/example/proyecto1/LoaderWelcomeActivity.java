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

public class LoaderWelcomeActivity extends AppCompatActivity {

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


        // fichero interno nota de prueba, luego esto quitar
        try {
            OutputStreamWriter fichero = new OutputStreamWriter(openFileOutput("nombrefichero" +
                            ".html",
                    Context.MODE_PRIVATE));
            fichero.write("<h1>Estoy escribiendo en el " +
                    "fichero asdfadsfa asdfsdf afasdf sdfasdf asdf asdf asdf " +
                    "asdfasdf asdf asdf asd fasd fs f</h1><br>Normal normal " +
                    "<br><b>basdf" +
                    " asd a sa f " +
                    "NEGRITA</b><br><a href='http://google.com'>Click Here</a>");
            fichero.write("aDF");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("<br>");
            fichero.write("adfasdf");

            fichero.close();
        } catch (IOException e){

        }

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
                        Intent i = new Intent(LoaderWelcomeActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        // user has yet to log in
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
