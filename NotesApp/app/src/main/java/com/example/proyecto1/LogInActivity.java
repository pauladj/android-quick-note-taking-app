package com.example.proyecto1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto1.fragments.AsyncTaskFragment;
import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.MyDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class LogInActivity extends Common {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide title bar
        setContentView(R.layout.login);
        //focus on username field when username sees the screen
        findViewById(R.id.inputUsername).requestFocus();
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
            FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        showToast(false, R.string.serverError);
                        return;
                    }

                    // Get new Instance ID token
                    String firebaseToken = task.getResult().getToken();
                    String[] params = {username, password, firebaseToken};
                    getmTaskFragment().setAction("login");
                    getmTaskFragment().start(params);
                })
                .addOnFailureListener(exception -> {
                    showToast(false, R.string.serverError);
                });
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
