package com.example.proyecto1;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;

import com.example.proyecto1.dialogs.DateDialog;
import com.example.proyecto1.dialogs.DeleteNoteDialog;
import com.example.proyecto1.dialogs.TimeDialog;
import com.example.proyecto1.fragments.SingleNoteFragment;
import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.MainToolbar;

public class SingleNoteActivity extends MainToolbar implements DeleteNoteDialog.ListenerDelDialogo, DateDialog.ListenerDate, TimeDialog.ListenerTime  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load activity with fragment
        setContentView(R.layout.single_note_activity);
        // load the top bar
        loadToolbar();

        // load the note information knowing the id
        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("id")){
            // it's from a notification
            NotificationManager elManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            elManager.cancel(extras.getInt("id"));

            // load again the current user
            String activeUsername = getActiveUsername();
            Data.getMyData().setActiveUsername(activeUsername);

        }
        super.setNoteId(extras.getInt("noteId", -1));


        SingleNoteFragment fragmentDemo = (SingleNoteFragment) getSupportFragmentManager().findFragmentById(R.id.singleNoteFragment);
        if (super.getNoteId() != -1){
            fragmentDemo.loadNote(super.getNoteId());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Change the topbar options
        getMenuInflater().inflate(R.menu.single_note_toolbar, menu);
        return true;
    }


    /**
     * Remove a note knowing its id
     */
    public void yesDeleteNote(){
        super.yesDeleteNote(super.getNoteId());
    }


}