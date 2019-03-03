package com.example.proyecto1;

import android.os.Bundle;

import com.example.proyecto1.utilities.MainToolbar;

public class MainActivity extends MainToolbar
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load main activity with fragments
        setContentView(R.layout.main_activity);
        // load top toolbar
        loadToolbar();
    }
}
