package com.example.proyecto1;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto1.fragments.AsyncTaskFragment;
import com.example.proyecto1.utilities.MyDB;
import com.google.android.gms.dynamic.SupportFragmentWrapper;

public class SignUpActivity extends LanguageActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide title bar
        setContentView(R.layout.signup);
        //focus on username field when username sees the screen
        findViewById(R.id.inputUsername).requestFocus();

    }

    /**
     * Click on log in button
     */
    public void logInButton(View view){
        goToLogIn();
    }

    /**
     * Go to the log in page
     */
    public void goToLogIn(){
        Intent i = new Intent(this, LogInActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * Click on sign up button
     */
    public void signUpButton(View view){
        // Sign up users
        EditText usernameField = findViewById(R.id.inputUsername);
        String username = usernameField.getText().toString();
        EditText passwordField = findViewById(R.id.inputPassword);
        String password = passwordField.getText().toString();

        if (username.trim().matches("") || password.trim().matches("")){
            // empty username or password, do nothing
        }else{
            // sign up
            String[] params = {username, password};
            getmTaskFragment().setAction("signup");
            getmTaskFragment().start(params);
        }
    }

}

