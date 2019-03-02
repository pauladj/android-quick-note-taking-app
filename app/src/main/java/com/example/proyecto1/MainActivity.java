package com.example.proyecto1;

import android.os.Bundle;

import com.example.proyecto1.utilities.MainToolbar;

public class MainActivity extends MainToolbar
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }
}
