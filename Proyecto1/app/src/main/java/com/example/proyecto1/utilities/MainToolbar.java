package com.example.proyecto1.utilities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.proyecto1.R;
import com.example.proyecto1.SingleNoteActivity;

public class MainToolbar extends AppCompatActivity {

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Loads top toolbar
     */
    protected void loadToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.labarra);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if (id == R.id.menuEdit){

        }else if(id == R.id.menuSendEmail){
            // Send note by email
            sendNoteByEmail();
        }else if(id == R.id.menuDelete){
            // Confirm with user that they want to delete the note
            confirmDeleteNote();
        }else if(id == R.id.menuFilter){

        }else if(id == R.id.menuSearch){

        }else if(id == R.id.menuSettings){

        }

        return super.onOptionsItemSelected(item);
    }


    public void confirmDeleteNote(){}
    public void sendNoteByEmail(){}

    /**
     * The specified menu option is shown
     * @param itemIdentifier - the id of the menu option to show
     */
    protected void showMenuOption(int itemIdentifier){
        menu.findItem(itemIdentifier).setVisible(true);
    }


}
