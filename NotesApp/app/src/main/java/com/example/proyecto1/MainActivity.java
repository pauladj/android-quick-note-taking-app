package com.example.proyecto1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto1.dialogs.AddRemoveTag;
import com.example.proyecto1.dialogs.DateDialog;
import com.example.proyecto1.dialogs.DeleteNoteDialog;
import com.example.proyecto1.dialogs.NewNoteType;
import com.example.proyecto1.dialogs.NewTag;
import com.example.proyecto1.dialogs.TimeDialog;
import com.example.proyecto1.fragments.NotesFragment;
import com.example.proyecto1.fragments.SingleNoteFragment;
import com.example.proyecto1.utilities.MainToolbar;
import com.example.proyecto1.utilities.MyDB;

import java.util.ArrayList;

public class MainActivity extends MainToolbar implements NotesFragment.listenerDelFragment,
        DeleteNoteDialog.ListenerDelDialogo, AddRemoveTag.ListenerDelDialogo,
        NewTag.ListenerDelDialogo, NewNoteType.ListenerNewNoteType, DateDialog.ListenerDate, TimeDialog.ListenerTime {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load main activity with fragment(s)
        setContentView(R.layout.main_activity);
        // load top toolbar
        loadToolbar();
    }


    /**
     * Remove a note knowing its id (toolbar)
     */
    public void yesDeleteNote(){
        super.yesDeleteNote(super.getNoteId());
    }


    /**
     * A note is clicked, this is the event that handles it
     * @param selectedNoteId - the selected note id
     */
    public void clickOnNote(int selectedNoteId){
        SingleNoteFragment fragment =
                (SingleNoteFragment) getSupportFragmentManager().findFragmentById(R.id.singleNoteFragment);
        if (fragment != null && fragment.isInLayout() == true){
            super.setNoteId(selectedNoteId);
            // landscape
            // add options to menu
            showMenuOption(R.id.menuDelete);
            showMenuOption(R.id.menuEdit);
            showMenuOption(R.id.menuSendEmail);
            showMenuOption(R.id.menuUploadToDrive);
            showMenuOption(R.id.menuCalendar);

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
        DialogFragment dialogChoose = new NewNoteType();
        dialogChoose.show(getSupportFragmentManager(), "chooseNoteType");
    }

    /**
     * The user has decided to create a normal note
     */
    @Override
    public void createNormalNote() {
        // crear nota normal
        Intent intent= new Intent(this, NoteEditorActivity.class);
        startActivityForResult(intent, 334);
    }

    /**
     * The user has decided to create a self note
     */
    @Override
    public void createSelfNote() {
        // obtener info para ver las notas rápidas, es decir, hacer un fetch para ver
        // si hay nuevas desde la última vez
        getmTaskFragment().setAction("fetchselfnotes");
        getmTaskFragment().start(null);
    }

    /**
     * We get the result of creating new note
     * @param requestCode - to specify this is the callback of creating a note
     * @param resultCode - the result code
     * @param data - the data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // New note result
        if (requestCode == 334) {
            if (resultCode == RESULT_OK) {
                // toast with ok
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(getApplicationContext(), R.string.successCreatingNote,
                        tiempo);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 100);
                aviso.show();
                // add it to recycler view
                NotesFragment fragment =
                        (NotesFragment) getSupportFragmentManager().findFragmentById(R.id.notesFragment);
                String fileName = data.getStringExtra("fileName"); //filename of the created note, it's unique
                fragment.addNote(fileName); // add just created note
            } else {
                // toast with fail
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(getApplicationContext(), R.string.failCreatingNote,
                        tiempo);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 100);
                aviso.show();
            }
        }
    }

    /**
     * Manage tags, add and remove them, open dialog
     */
    public void manageTags(){
        // Show the dialog
        DialogFragment addRemoveTag = new AddRemoveTag();
        addRemoveTag.show(getSupportFragmentManager(), "addRemoveDialog");
    }

    /**
     * The user has selected the tags to delete, delete them
     * @param tagsId - the ids of the tags to delete
     */
    @Override
    public void yesRemoveTags(ArrayList<Integer> tagsId) {
        MyDB gestorDB = new MyDB(getApplicationContext(), "Notes", null, 1);
        for (int id : tagsId){
            ArrayList<Integer> noteIds = gestorDB.removeTag(id); // remove tag, get the noteids using that tag
            if (noteIds == null){
                // database error
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(this, R.string.databaseError, tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();
                break;
            }else{
                // update recycler view
                NotesFragment fragment =
                        (NotesFragment) getSupportFragmentManager().findFragmentById(R.id.notesFragment);
                for (int noteId : noteIds){ // the noteids using that tag
                    fragment.changeNote(noteId);
                }
            }

        }


    }



}
