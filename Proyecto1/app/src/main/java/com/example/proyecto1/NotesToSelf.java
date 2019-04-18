package com.example.proyecto1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto1.cardview.ElAdaptadorRecycler;
import com.example.proyecto1.selfNotesCardView.ElAdaptadorRecyclerSelfNotes;
import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.MainToolbar;
import com.example.proyecto1.utilities.MyDB;

import java.util.ArrayList;
import java.util.Collections;

public class NotesToSelf extends MainToolbar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_to_self);

        // load recycler view
        RecyclerView layoutRecycler = findViewById(R.id.notesToSelf);

        // load self notes
        String activeUser = Data.getMyData().getActiveUsername();
        MyDB gestorDB = new MyDB(this, "Notes", null, 1);
        ArrayList<ArrayList<String>> notesData = gestorDB.getSelfNotesByUser(activeUser); //
        // text, date, image

        if (notesData == null){
            // database error
            int tiempo = Toast.LENGTH_SHORT;
            Toast aviso = Toast.makeText(this, R.string.databaseError, tiempo);
            aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
            aviso.show();
        }else {
            // titles, dates, tags

            final ElAdaptadorRecyclerSelfNotes eladaptador = new ElAdaptadorRecyclerSelfNotes(notesData.get(0),
                    notesData.get(1), notesData.get(2));

            // Add listeners
            eladaptador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = layoutRecycler.getChildAdapterPosition(v);

                    String imagePath = notesData.get(2).get(clickedPosition);

                    clickOnSelfNote(imagePath);

                }
            });
            layoutRecycler.setAdapter(eladaptador);
            LinearLayoutManager elLayoutLineal = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            layoutRecycler.setLayoutManager(elLayoutLineal);
            eladaptador.notifyDataSetChanged();
        }
    }

    /**
     * A self note has been clicked
     * @param imagePath - the path of the image
     */
    private void clickOnSelfNote(String imagePath){
        if (imagePath != null && !imagePath.isEmpty()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(imagePath), "image/*");
            startActivity(intent);
        }
    }
}
