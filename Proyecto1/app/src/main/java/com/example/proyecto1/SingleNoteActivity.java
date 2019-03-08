package com.example.proyecto1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.proyecto1.fragments.SingleNoteFragment;
import com.example.proyecto1.utilities.MainToolbar;

public class SingleNoteActivity extends MainToolbar {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Change the topbar options
        getMenuInflater().inflate(R.menu.single_note_toolbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load activity with fragment
        setContentView(R.layout.single_note_activity);
        // load the top bar
        loadToolbar();

        // load the note information knowing the id
        int noteId = getIntent().getIntExtra("noteId", 1);
        SingleNoteFragment fragmentDemo = (SingleNoteFragment) getSupportFragmentManager().findFragmentById(R.id.singleNoteFragment);
        fragmentDemo.loadNote(noteId);
    }

}