package com.example.proyecto1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SignUpActivity extends AppCompatActivity {

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
        Intent i = new Intent(this, LogInActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * Click on sign up button
     */
    public void signUpButton(View view){
        // do whatever
        logInButton(view);
    }
}
