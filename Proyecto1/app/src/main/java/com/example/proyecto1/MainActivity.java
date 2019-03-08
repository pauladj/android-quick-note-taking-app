package com.example.proyecto1;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        Log.i("aqui", "onconfiguration");
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.i("aqui", "portrait");
            SingleNoteFragment elotro =
                    (SingleNoteFragment) getSupportFragmentManager().findFragmentById(R.id.singleNoteFragment);
            if(elotro!=null){
                getSupportFragmentManager().beginTransaction().remove(elotro).commit();
            }
        }
    }

    /**
     * A note is click, this is the event that handles it
     * @param selectedNoteId - the selected note id
     */
    public void clickOnNote(int selectedNoteId){
        SingleNoteFragment fragment =
                (SingleNoteFragment) getSupportFragmentManager().findFragmentById(R.id.singleNoteFragment);
        if (fragment != null && fragment.isInLayout() == true){
            //EL OTRO FRAGMENT EXISTE
            Log.i("aqui", "distinto a null");
            SingleNoteFragment elotro = (SingleNoteFragment) getSupportFragmentManager().
                    findFragmentById(R.id.singleNoteFragment);
            elotro.loadNote(selectedNoteId);
        }
        else{
            Log.i("aqui", "no");
            //EL OTRO FRAGMENT NO EXISTE, HAY QUE LANZAR LA ACTIVIDAD QUE LO CONTIENE
            Intent i= new Intent(this, SingleNoteActivity.class);
            i.putExtra("noteId", selectedNoteId);
            startActivity(i);

        }
    }
}
