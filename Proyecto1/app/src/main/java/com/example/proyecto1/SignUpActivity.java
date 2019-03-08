package com.example.proyecto1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto1.utilities.MyDB;

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
        // Sign up users
        EditText usernameField = findViewById(R.id.inputUsername);
        String username = usernameField.getText().toString();
        EditText passwordField = findViewById(R.id.inputPassword);
        String password = passwordField.getText().toString();

        if (username.trim().matches("") || password.trim().matches("")){
            // empty username or password, do nothing
        }else{
            // sign up
            MyDB gestorDB = new MyDB(this, "Notes", null, 1);
            Boolean usernameExists = gestorDB.checkIfUsernameExists(username);
            if (usernameExists){
                // toast saying it exists
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(this, R.string.userAlreadyExists, tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();
            }else{
                // Save the new user
                SQLiteDatabase db = gestorDB.getWritableDatabase();
                ContentValues newUser = new ContentValues();
                newUser.put("username", username);
                newUser.put("password", password);
                db.insert("Users", null, newUser);
                db.close();

                // show toast across screens
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(getApplicationContext(), R.string.userSuccessfullyRegistered, tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();

                // and then show log in screen
                logInButton(view);
            }
        }
    }
}
