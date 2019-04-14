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

public class SignUpActivity extends LanguageActivity implements AsyncTaskFragment.TaskCallbacks {

    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private AsyncTaskFragment mTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide title bar
        setContentView(R.layout.signup);
        //focus on username field when username sees the screen
        findViewById(R.id.inputUsername).requestFocus();

        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (AsyncTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new AsyncTaskFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

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
            Log.i("aquiw", "asdf");
            String[] params = {"signup", username, password};
            mTaskFragment.start(params);

            /*
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
            }*/
        }
    }

}

