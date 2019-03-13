package com.example.proyecto1.utilities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.proyecto1.MainActivity;
import com.example.proyecto1.NoteEditorActivity;
import com.example.proyecto1.R;
import com.example.proyecto1.SingleNoteActivity;
import com.example.proyecto1.dialogs.DeleteNoteDialog;
import com.example.proyecto1.fragments.NotesFragment;
import com.example.proyecto1.fragments.SingleNoteFragment;

public class MainToolbar extends AppCompatActivity {

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Loads top toolbar
     */
    public void loadToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.labarra);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        this.menu = menu;
        return true;
    }

    /**
     * It adds a back arrow to the option menu
     */
    public void showBackButtonOption(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if (id == R.id.menuEdit){
            // Edit a note
            editNote();
        }else if(id == R.id.menuSendEmail){
            // Send note by email
            sendNoteByEmail();
        }else if(id == R.id.menuDelete){
            // Confirm with user that they want to delete the note
            confirmDeleteNote();
        }else if(id == R.id.menuFilter){

        }else if(id == R.id.menuSearch){

        }else if(id == R.id.menuSettings){

        }else if(id == R.id.menuSave){
            // Save note when editing it
            saveNote();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * The specified menu option is shown
     * @param itemIdentifier - the id of the menu option to show
     */
    protected void showMenuOption(int itemIdentifier){
        menu.findItem(itemIdentifier).setVisible(true);
    }


    // -----------  Send note as email
    /**
     * Send the current note by email
     */
    public void sendNoteByEmail(){
        SingleNoteFragment fragment = (SingleNoteFragment) getSupportFragmentManager().findFragmentById(R.id.singleNoteFragment);
        String content = fragment.getNoteContent();

        Spanned plainText = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            plainText = Html.fromHtml(content,
                    Html.FROM_HTML_MODE_COMPACT);
        } else {
            plainText = Html.fromHtml(content);
        }

        String uriText = "mailto:?body=" + plainText.toString();
        Uri uri = Uri.parse(uriText);

        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(uri);
        try {
            String chooseEmailClientText = getResources().getString(R.string.chooseEmailClient);
            startActivity(Intent.createChooser(i, chooseEmailClientText));
        } catch (android.content.ActivityNotFoundException ex) {
            String noApp = getResources().getString(R.string.noAppForThis);
            Toast.makeText(this,noApp, Toast.LENGTH_LONG).show();
        }
    }


    // -----------  Delete note
    /**
     * Confirm if the note has to be deleted
     */
    public void confirmDeleteNote(){
        // Show the dialog to confirm
        DialogFragment confirmationDialog = new DeleteNoteDialog();
        confirmationDialog.show(getSupportFragmentManager(), "deleteNoteDialog");
    }

    /**
     * The user wants to delete the note
     * @param noteId - the id of the note to remove
     */
    public void yesDeleteNote(int noteId){
        MyDB gestorDB = new MyDB(getApplicationContext(), "Notes", null, 1);
        gestorDB.deleteANote(noteId);

        // show toast across screens
        int tiempo = Toast.LENGTH_SHORT;
        Toast aviso = Toast.makeText(getApplicationContext(), R.string.noteSuccessfullyDeleted,
                tiempo);
        aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
        aviso.show();

        Intent i = new Intent (this, MainActivity.class);
        // clear the activity stack
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    /**
     * The user wants to save an edited note
     */
    public void saveNote(){}

    /**
     * The user wants to edit a note
     */
    public void editNote(){}

    /**
     * Call the activity to edit a note
     * @param noteId - the id of the note to edit
     */
    public void editNote(int noteId){
        Intent intent= new Intent(this, NoteEditorActivity.class);
        intent.putExtra("noteId", noteId);
        startActivityForResult(intent, 333);
    }

    /**
     * We get the result of editing a note
     * @param requestCode - to specify this is the callback of editing a note
     * @param resultCode - the result code
     * @param data - the data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // New note result
        if (requestCode == 333){
            if (resultCode == RESULT_OK) {
                // toast with ok
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(getApplicationContext(), R.string.successEditingNote,
                        tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();

                Intent i = new Intent (this, MainActivity.class);
                // clear the activity stack
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }else {
                // toast with fail
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(getApplicationContext(), R.string.failSavingNote,
                        tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();
            }
        }
    }

}
