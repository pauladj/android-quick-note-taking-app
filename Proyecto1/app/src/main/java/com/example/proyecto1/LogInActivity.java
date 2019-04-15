package com.example.proyecto1;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto1.fragments.AsyncTaskFragment;
import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.MyDB;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class LogInActivity extends LanguageActivity {

    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private AsyncTaskFragment mTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide title bar
        setContentView(R.layout.login);
        //focus on username field when username sees the screen
        findViewById(R.id.inputUsername).requestFocus();

        // fragmento que contiene la tarea asíncrona
        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (AsyncTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // El fragmento solo es null cuando la actividad se crea por primera vez, cuando se rota
        // el fragmento se mantiene
        if (mTaskFragment == null) {
            mTaskFragment = new AsyncTaskFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }
    }

    /**
     * Click on log in button
     * @param view
     */
    public void logInButton(View view) {
        // check if user and password ok
        EditText usernameField = findViewById(R.id.inputUsername);
        String username = usernameField.getText().toString();
        EditText passwordField = findViewById(R.id.inputPassword);
        String password = passwordField.getText().toString();

        if (username.trim().matches("") || password.trim().matches("")) {
            // empty username or password, do nothing
        } else {
            // log in
            String[] params = {username, password};
            mTaskFragment.setAction("login");
            mTaskFragment.start(params);

            /*
            MyDB gestorDB = new MyDB(this, "Notes", null, 1);
            boolean userCanBeLoggedIn = gestorDB.checkIfUserCanBeLoggedIn(username, password);
            if (userCanBeLoggedIn) {
                // there is a user with this data
                // save the user as active in database
                boolean changed = gestorDB.setUsernameAsActive(username);

                if (changed){
                    // set the active username so we don't have to read it from the database every time
                    Data.getMyData().setActiveUsername(username);

                    // go to the main activity
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    // database error
                    int tiempo = Toast.LENGTH_SHORT;
                    Toast aviso = Toast.makeText(this, R.string.databaseError, tiempo);
                    aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                    aviso.show();
                }


            } else {
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(this, R.string.incorrectPassword, tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();
            }*/
        }
    }

    /**
     * Click on sign up button
     * @param view
     */
    public void signUpButton(View view){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
        finish();
    }

}
