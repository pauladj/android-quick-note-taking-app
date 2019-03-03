package com.example.proyecto1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto1.utilities.ActiveUser;
import com.example.proyecto1.utilities.MyDB;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.crypto.Cipher;

public class LogInActivity extends AppCompatActivity {

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
            MyDB gestorDB = new MyDB(this, "Notes", null, 1);
            Boolean userCanBeLoggedIn = gestorDB.checkIfUserCanBeLoggedIn(username, password);
            if (userCanBeLoggedIn) {
                // there is a user with this data
                // save internal file saying that the user is logged in
                try {
                    OutputStreamWriter fichero = new OutputStreamWriter(openFileOutput("activeUser",
                            Context.MODE_PRIVATE));
                    fichero.write(username);
                    fichero.close();
                } catch (IOException e){
                    // do nothing
                }

                // set the active username so we don't have to read it from the file every time
                ActiveUser.getMyActiveUsername().setUsername(username);

                // go to the main activity
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(this, R.string.incorrectPassword, tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();
            }
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
