package com.example.proyecto1;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.example.proyecto1.dialogs.DeleteNoteDialog;
import com.example.proyecto1.fragments.NotesFragment;
import com.example.proyecto1.fragments.SingleNoteFragment;
import com.example.proyecto1.utilities.MainToolbar;

import org.w3c.dom.Text;

public class MainActivity extends MainToolbar implements NotesFragment.listenerDelFragment,
        DeleteNoteDialog.ListenerDelDialogo  {

    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load main activity with fragment(s)
        setContentView(R.layout.main_activity);
        // load top toolbar
        loadToolbar();
    }


    /**
     * Save the noteId so it won't lose if there is a rotation of screen
     * @param savedInstanceState
     */
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("noteId", noteId);
    }

    /**
     * Restore the noteId value
     * @param savedInstanceState
     */
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        noteId = savedInstanceState.getInt("noteId");
    }

    /**
     * Remove a note knowing its id (toolbar)
     */
    public void yesDeleteNote(){
        super.yesDeleteNote(noteId);
    }

    /**
     * A note is click, this is the event that handles it
     * @param selectedNoteId - the selected note id
     */
    public void clickOnNote(int selectedNoteId){
        SingleNoteFragment fragment =
                (SingleNoteFragment) getSupportFragmentManager().findFragmentById(R.id.singleNoteFragment);
        if (fragment != null && fragment.isInLayout() == true){
            noteId = selectedNoteId;
            // landscape
            // add options to menu
            showMenuOption(R.id.menuDelete);
            showMenuOption(R.id.menuEdit);
            showMenuOption(R.id.menuSendEmail);

            SingleNoteFragment elotro = (SingleNoteFragment) getSupportFragmentManager().
                    findFragmentById(R.id.singleNoteFragment);
            // reload fragment info
            elotro.loadNote(selectedNoteId);

            // Color the background to show the element is selected
            NotesFragment notes =
                    (NotesFragment) getSupportFragmentManager().findFragmentById(R.id.notesFragment);
            notes.markAsSelected();
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
