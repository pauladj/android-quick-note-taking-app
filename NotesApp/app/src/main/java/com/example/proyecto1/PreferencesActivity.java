package com.example.proyecto1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.example.proyecto1.fragments.PreferencesFragment;
import com.example.proyecto1.utilities.MainToolbar;

public class PreferencesActivity extends MainToolbar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_activity);
        // load the top bar
        loadToolbar();
        // show the back button
        showBackButtonOption();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Change the topbar options
        getMenuInflater().inflate(R.menu.preferences_toolbar, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // If the back button is pressed
        Intent i = new Intent (this, MainActivity.class);
        // clear the activity stack, so the mainactivity view is recreated
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();

    }
}
