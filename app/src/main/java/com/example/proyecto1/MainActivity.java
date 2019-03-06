package com.example.proyecto1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.proyecto1.fragments.NotesFragment;
import com.example.proyecto1.fragments.SingleNoteFragment;
import com.example.proyecto1.utilities.MainToolbar;

public class MainActivity extends MainToolbar implements NotesFragment.listenerDelFragment {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load main activity with fragment(s)
        setContentView(R.layout.main_activity);
        // load top toolbar
        loadToolbar();
    }

    /**
     * A note is click, this is the event that handles it
     * @param selectedNoteId - the selected note id
     */
    public void clickOnNote(int selectedNoteId){
        if (getSupportFragmentManager().findFragmentById(R.id.singleNoteFragment) != null){
            //EL OTRO FRAGMENT EXISTE
            SingleNoteFragment elotro = (SingleNoteFragment) getSupportFragmentManager().
                    findFragmentById(R.id.singleNoteFragment);
            elotro.loadNote(selectedNoteId);
        }
        else{
            //EL OTRO FRAGMENT NO EXISTE, HAY QUE LANZAR LA ACTIVIDAD QUE LO CONTIENE
            Intent i= new Intent(this, SingleNoteActivity.class);
            i.putExtra("noteId", selectedNoteId);
            startActivity(i);
        }
    }
}
