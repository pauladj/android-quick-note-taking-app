package com.example.proyecto1;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto1.dialogs.DeleteNoteDialog;
import com.example.proyecto1.fragments.NotesFragment;
import com.example.proyecto1.fragments.SingleNoteFragment;
import com.example.proyecto1.utilities.MainToolbar;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends MainToolbar implements NotesFragment.listenerDelFragment,
        DeleteNoteDialog.ListenerDelDialogo  {

    private int noteId; // selected note in landscape mode

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
        }
        else{
            // Portrait
            //EL OTRO FRAGMENT NO EXISTE, HAY QUE LANZAR LA ACTIVIDAD QUE LO CONTIENE
            Intent i= new Intent(this, SingleNoteActivity.class);
            i.putExtra("noteId", selectedNoteId);
            startActivity(i);
        }
    }

    /**
     * The user wants to create a new note
     * @param view - the clicked on element
     */
    public void createNewNote(View view) {
        Intent intent= new Intent(MainActivity.this, NoteEditorActivity.class);
        startActivityForResult(intent, 666);
    }

    /**
     * We get the result of creating new note
     * @param requestCode - to specify this is the callback of creating a note
     * @param resultCode - the result code
     * @param data - the data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // New note result
        if (requestCode == 666){
            if (resultCode == RESULT_OK) {
                // toast with ok
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(getApplicationContext(), R.string.successCreatingNote,
                        tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();
                // add it to recycler view
                NotesFragment fragment =
                        (NotesFragment) getSupportFragmentManager().findFragmentById(R.id.notesFragment);
                String fileName = data.getStringExtra("fileName"); //filename of the created note, it's unique
                fragment.addNote(fileName); // add just created note
            }else {
                // toast with fail
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(getApplicationContext(), R.string.failCreatingNote,
                        tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();
            }
        }
    }
}
