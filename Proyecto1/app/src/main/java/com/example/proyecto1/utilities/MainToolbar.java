package com.example.proyecto1.utilities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.proyecto1.R;
import com.example.proyecto1.SingleNoteActivity;

public class MainToolbar extends AppCompatActivity {

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            // main
            case R.id.menuFilter:{

            }
            case R.id.menuSearch:{

            }
            // single note
            case R.id.menuDelete:{
                // Confirm with user that they want to delete the note
                confirmDeleteNote();
            }
            case R.id.menuEdit:{

            }
            case R.id.menuSendEmail:{
                // Send note by email
                sendNoteByEmail();
            }
            // settings
            case R.id.menuSettings:{

            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void confirmDeleteNote(){}
    public void sendNoteByEmail(){}


}
