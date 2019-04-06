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

import com.example.proyecto1.LanguageActivity;
import com.example.proyecto1.LogInActivity;
import com.example.proyecto1.MainActivity;
import com.example.proyecto1.NoteEditorActivity;
import com.example.proyecto1.PreferencesActivity;
import com.example.proyecto1.R;
import com.example.proyecto1.SingleNoteActivity;
import com.example.proyecto1.dialogs.DeleteNoteDialog;
import com.example.proyecto1.dialogs.NewTag;
import com.example.proyecto1.fragments.NotesFragment;
import com.example.proyecto1.fragments.PreferencesFragment;
import com.example.proyecto1.fragments.SingleNoteFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.services.drive.DriveScopes;

import java.io.File;
import java.io.IOException;

public class MainToolbar extends LanguageActivity {

    Menu menu;
    int noteId = -1; //selected noteid for singleNoteActivity and MainActivity (the one selected in the landscape mode)

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
        // Mostrar flecha para ir para atrás si se quiere
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Si se pulsa en la flecha del toolbar se va para atrás
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if (id == R.id.menuEdit){
            // Edit a note
            editNote();
        }else if(id == R.id.menuSendEmail) {
            // Send note by email
            sendNoteByEmail();
        }else if(id == R.id.menuUploadToDrive){
            // Upload note to drive
            logInToDrive();
        }else if(id == R.id.menuDelete){
            // Confirm with user that they want to delete the note
            confirmDeleteNote();
        }else if(id == R.id.menuTags){
            // Manage tags, add and remove
            manageTags();
        }else if(id == R.id.menuSettings){
            // The user wants to change the settings
            settings();
        }else if(id == R.id.menuLogout){
            // The user wants to log out
            logOut();
        }else if(id == R.id.menuSave){
            // Save note when editing it
            saveNote();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set the noteId
     * @param noteId
     */
    public void setNoteId(int noteId){
        this.noteId = noteId;
    }

    /**
     * Get the noteId
     */
    public int getNoteId(){
        return noteId;
    }


    /**
     * The specified menu option is shown
     * @param itemIdentifier - the id of the menu option to show
     */
    protected void showMenuOption(int itemIdentifier){
        menu.findItem(itemIdentifier).setVisible(true);
    }


    /**
     * Send the current note by email
     */
    public void sendNoteByEmail(){
        SingleNoteFragment fragment = (SingleNoteFragment) getSupportFragmentManager().findFragmentById(R.id.singleNoteFragment);
        String content = fragment.getNoteContent(); // se obtiene el contenido de la nota en
        // texto plano

        Spanned plainText = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            plainText = Html.fromHtml(content,
                    Html.FROM_HTML_MODE_COMPACT);
        } else {
            plainText = Html.fromHtml(content);
        }

        // el mensaje se configura
        String uriText = "mailto:?body=" + plainText.toString();
        Uri uri = Uri.parse(uriText);
        // se crea el intent
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(uri);
        try {
            // el usuario puede elegir la aplicación, si no tiene ninguna se le muestra mensaje
            // de error
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
        String fileName = gestorDB.getNoteFileName(noteId);
        if (fileName == null){
            // database error
            int tiempo = Toast.LENGTH_SHORT;
            Toast aviso = Toast.makeText(this, R.string.databaseError, tiempo);
            aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
            aviso.show();
            return;
        }
        try {
            // delete the note file
            File dir = getFilesDir();
            File file = new File(dir, fileName);
            boolean deleted = file.delete();

            if (deleted == false){
                throw new IOException();
            }

            deleted = gestorDB.deleteANote(noteId);

            if (deleted){
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
            }else {
                // database error
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(this, R.string.databaseError, tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();
            }

        } catch (IOException e) {
            int tiempo = Toast.LENGTH_SHORT;
            Toast aviso = Toast.makeText(this, R.string.failDeletingNote, tiempo);
            aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
            aviso.show();
        }



    }

    /**
     * The user wants to change the preferences
     */
    private void settings(){
        Intent i = new Intent (this, PreferencesActivity.class);
        startActivity(i);
    }

    /**
     * The user wants to save an edited note
     */
    public void saveNote(){}


    /**
     * The user wants to manage the tags
     */
    public void manageTags(){}



    /**
     * Try to log in into Google Drive
     */
    public void logInToDrive(){
        GoogleSignInAccount cuenta = GoogleSignIn.getLastSignedInAccount(this);
        if (cuenta == null){
            // no está identificado
            GoogleSignInOptions gso = new
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignInClient cliente= GoogleSignIn.getClient(this, gso);
            // el sistema lo gestiona
            Intent intentIdentif = cliente.getSignInIntent();
            startActivityForResult(intentIdentif, 666);
        }else{
            requestPermissionsToDrive();
        }
    }

    /**
     * Request permissions to Drive
     */
    public void requestPermissionsToDrive(){
        Scope permiso = new Scope(DriveScopes.DRIVE);
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), permiso)) {
            GoogleSignIn.requestPermissions(this, 667,
                    GoogleSignIn.getLastSignedInAccount(this), permiso);
        }else{
            uploadNoteToDrive();
        }
    }


    /**
     * Upload the note content to drive
     */
    public void uploadNoteToDrive(){
        //sklfdj
    }


    /**
     * Call the activity to edit a note and wait for the result
     * @param noteId - the id of the note to edit
     */
    public void editNote(){
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

        // Edit note result
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
        }else if(requestCode == 666){
            // attempt to log in into google drive
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // identificado correctamente
                GoogleSignInAccount cuenta = task.getResult(ApiException.class);
                requestPermissionsToDrive();
            } catch (ApiException e) {
                Log.i("aqui", e.toString());
                Log.i("aqui", e.getMessage());
                // fallo de identificación
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(getApplicationContext(), R.string.googleDriveLogInError, tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();
            }
        }else if(requestCode == 667){
            // attempt to get user permission
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount cuenta = task.getResult(ApiException.class);
                uploadNoteToDrive();
            } catch (ApiException e) {
                // fallo de identificación
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(getApplicationContext(), R.string.googleDriveLogInError, tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();
            }
        }
    }

    /**
     * The user wants to create a new tag
     */
    public void createNewTag(){
        // open dialog
        DialogFragment dialog = new NewTag();
        dialog.show(getSupportFragmentManager(), "newTag");
    }

    /**
     * The user has entered a name for the new tag, save it for the current user
     * @param tagName - the chosen name
     */
    public void yesNewTag(String tagName){
        MyDB gestorDB = new MyDB(getApplicationContext(), "Notes", null, 1);
        boolean exists = gestorDB.tagExists(Data.getMyData().getActiveUsername(), tagName);
        if (exists){
            // the tag exists, alert the user
            int tiempo = Toast.LENGTH_SHORT;
            Toast aviso = Toast.makeText(getApplicationContext(), R.string.failTagExists,
                    tiempo);
            aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
            aviso.show();
        }else {
            boolean added = gestorDB.addTag(Data.getMyData().getActiveUsername(), tagName);
            if (added == false){
                // database error
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(this, R.string.databaseError, tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();
            }
        }

    }

    /**
     * The user wants to log out
     */
    private void logOut(){
        MyDB gestorDB = new MyDB(getApplicationContext(), "Notes", null, 1);
        boolean changed = gestorDB.setUsernameAsInactive(Data.getMyData().getActiveUsername()); //
        // remove active
        // user
        if (changed){
            Data.getMyData().setActiveUsername(null); // remove active user
            // start login screen
            Intent i = new Intent(this, LogInActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);// Limpiar pila de actividades
            startActivity(i);
            finish();
        }else{
            // database error
            int tiempo = Toast.LENGTH_SHORT;
            Toast aviso = Toast.makeText(this, R.string.databaseError, tiempo);
            aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
            aviso.show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("noteId", noteId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        noteId = savedInstanceState.getInt("noteId", -1);
    }
}
