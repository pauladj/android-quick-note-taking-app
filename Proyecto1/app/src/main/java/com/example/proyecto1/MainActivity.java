package com.example.proyecto1;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.example.proyecto1.fragments.NotesFragment;
import com.example.proyecto1.fragments.SingleNoteFragment;
import com.example.proyecto1.utilities.MainToolbar;

import org.w3c.dom.Text;

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
        SingleNoteFragment fragment =
                (SingleNoteFragment) getSupportFragmentManager().findFragmentById(R.id.singleNoteFragment);
        if (fragment != null && fragment.isInLayout() == true){
            Log.i("aqui", "bien4");
            // landscape
            showMenuOption(R.id.menuDelete); // show delete button
            showMenuOption(R.id.menuEdit); // show edit button
            showMenuOption(R.id.menuSendEmail); // show send email button

            SingleNoteFragment elotro = (SingleNoteFragment) getSupportFragmentManager().
                    findFragmentById(R.id.singleNoteFragment);
            elotro.loadNote(selectedNoteId);
        }
        else{
            Log.i("aqui", "bien2");
            // Portrait
            //EL OTRO FRAGMENT NO EXISTE, HAY QUE LANZAR LA ACTIVIDAD QUE LO CONTIENE
            newSingleNoteActivity(selectedNoteId);
        }
    }

    /**
     * Creates new SingleNoteActivity knowing the noteid
     * @param selectedNoteId - the selected noteid
     */
    private void newSingleNoteActivity(int selectedNoteId){
        Intent i= new Intent(this, SingleNoteActivity.class);
        i.putExtra("noteId", selectedNoteId);
        startActivity(i);
    }
}
