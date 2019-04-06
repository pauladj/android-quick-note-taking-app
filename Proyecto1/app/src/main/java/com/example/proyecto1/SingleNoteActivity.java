package com.example.proyecto1;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto1.dialogs.ConfimExit;
import com.example.proyecto1.dialogs.DeleteNoteDialog;
import com.example.proyecto1.fragments.NotesFragment;
import com.example.proyecto1.fragments.SingleNoteFragment;
import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.MainToolbar;
import com.example.proyecto1.utilities.MyDB;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class SingleNoteActivity extends MainToolbar implements DeleteNoteDialog.ListenerDelDialogo {

    int noteId;
    
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
            MyDB gestorDB = new MyDB(this, "Notes", null, 1);
            String activeUsername = gestorDB.getActiveUsername();
            Data.getMyData().setActiveUsername(activeUsername);

        }
        noteId = extras.getInt("noteId", -1);


        SingleNoteFragment fragmentDemo = (SingleNoteFragment) getSupportFragmentManager().findFragmentById(R.id.singleNoteFragment);
        if (noteId != -1){
            fragmentDemo.loadNote(noteId);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Change the topbar options
        getMenuInflater().inflate(R.menu.single_note_toolbar, menu);
        return true;
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
     * Remove a note knowing its id
     */
    public void yesDeleteNote(){
        super.yesDeleteNote(noteId);

        
    }

    /**
     * Edit a note
     */
    public void editNote(){
        super.editNote(noteId);
    }


    /**
     * Upload note to drive
     */
    public void uploadNoteToDrive(){
        GoogleSignInAccount cuenta = GoogleSignIn.getLastSignedInAccount(this);
        if (cuenta == null){
            // no está identificado
            super.logInToDrive();
        }else{
            super.uploadNoteToDrive(noteId);
        }
    }

    /**
     * We get the callback of drive sign in
     * @param requestCode - to specify this is the callback of creating a note
     * @param resultCode - the result code
     * @param data - the data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // google drive log in attempt
       if(requestCode == 333){
            super.callbackDriveSignIn(data, noteId);
        }
    }

}