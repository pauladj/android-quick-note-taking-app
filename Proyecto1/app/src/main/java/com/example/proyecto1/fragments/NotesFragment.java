package com.example.proyecto1.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyecto1.R;
import com.example.proyecto1.cardview.ElAdaptadorRecycler;
import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.MyDB;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class NotesFragment extends Fragment {

    private RecyclerView notes;

    private ArrayList<String> notesIds = new ArrayList<>();
    private ArrayList<String> notesTitles = new ArrayList<>();
    private ArrayList<String> notesDates = new ArrayList<>();
    private ArrayList<String> notesTags = new ArrayList<>();




    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment

        View v = inflater.inflate(R.layout.notes_fragment, parent, false);
        return v;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onActivityCreated(savedInstanceState);
        // load recycler view
        notes = getView().findViewById(R.id.reciclerView);

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey("notesIds")){
                notesIds = savedInstanceState.getStringArrayList("notesIds");
                notesTitles = savedInstanceState.getStringArrayList("notesTitles");
                notesDates = savedInstanceState.getStringArrayList("notesDates");
                notesTags = savedInstanceState.getStringArrayList("notesTags");
            }
        }

        if (notesIds.isEmpty()){ // first time loading the activity, get the data
            // load notes
            String activeUser = Data.getMyData().getActiveUsername();
            MyDB gestorDB = new MyDB(getActivity(), "Notes", null, 1);
            ArrayList<ArrayList<String>> notesData = gestorDB.getNotesDataByUser(activeUser); // id,
            // titles, dates, tags

            if (notesData == null){
                // database error
                int tiempo = Toast.LENGTH_SHORT;
                Toast aviso = Toast.makeText(getActivity(), R.string.databaseError, tiempo);
                aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                aviso.show();

                notesIds = new ArrayList<>();
                notesTitles = new ArrayList<>();
                notesDates = new ArrayList<>();
                notesTags = new ArrayList<>();
            }else {
                notesIds = notesData.get(0);
                notesTitles = notesData.get(1);
                notesDates = notesData.get(2);
                notesTags = notesData.get(3);

                // Show the recent notes first?
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                boolean showRecentFirst = prefs.getBoolean("orden", false);

                if (showRecentFirst){
                    Collections.reverse(notesIds);
                    Collections.reverse(notesTitles);
                    Collections.reverse(notesDates);
                    Collections.reverse(notesTags);
                }
            }
        }

        // titles, dates, tags
        final ElAdaptadorRecycler eladaptador = new ElAdaptadorRecycler(notesTitles,
                notesDates, notesTags);

        // Add listeners
        eladaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = notes.getChildAdapterPosition(v);

                int noteIdOfPosition = Integer.valueOf(notesIds.get(clickedPosition));

                elListener.clickOnNote(noteIdOfPosition);

            }
        });
        notes.setAdapter(eladaptador);
        LinearLayoutManager elLayoutLineal= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        notes.setLayoutManager(elLayoutLineal);
        eladaptador.notifyDataSetChanged();
    }



    /**
     * Add new note to the adapter/Recyclerview
     * @param fileName
     */
    public void addNote(String fileName){
        MyDB gestorDB = new MyDB(getActivity(), "Notes", null, 1);
        ArrayList<String> noteData = gestorDB.getLastAddedNoteData(fileName);
        // get the last
        // added note
        // and append the info

        if (noteData == null){
            // database error
            int tiempo = Toast.LENGTH_SHORT;
            Toast aviso = Toast.makeText(getActivity(), R.string.databaseError, tiempo);
            aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
            aviso.show();
            return; //exit method
        }

        // Show the recent notes first?
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean showRecentFirst = prefs.getBoolean("orden", false);
        int posChanged;
        if (showRecentFirst){
            notesIds.add(0, noteData.get(0));
            notesTitles.add(0, noteData.get(1));
            notesDates.add(0, noteData.get(2));
            notesTags.add(0, noteData.get(3));
            posChanged = 0;
        }else{
            notesIds.add(noteData.get(0));
            notesTitles.add(noteData.get(1));
            notesDates.add(noteData.get(2));
            notesTags.add(noteData.get(3));
            posChanged = notesIds.size()-1;
        }

        // notify the adapter that the data has changed
        notes.getAdapter().notifyItemInserted(posChanged);
    }


    /**
     * Notify the adapter that a note has changed
     * @param id
     */
    public void changeNote(int id){
        MyDB gestorDB = new MyDB(getActivity(), "Notes", null, 1);
        String[] noteData = gestorDB.getNoteData(id);

        if (noteData != null){
            // get the changed note data
            // and change the info
            int indexOfChangedNote = notesIds.indexOf(String.valueOf(id));
            if (indexOfChangedNote != -1){

                notesIds.set(indexOfChangedNote, String.valueOf(id)); // noteid
                notesTitles.set(indexOfChangedNote, noteData[0]); // title
                notesDates.set(indexOfChangedNote, noteData[4]); // date
                notesTags.set(indexOfChangedNote, noteData[2]); // tag, if it doesn't have one, it's
                // null
                // notify the adapter that the data has changed
                notes.getAdapter().notifyItemChanged(indexOfChangedNote);
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("notesIds", notesIds);
        outState.putStringArrayList("notesTitles", notesTitles);
        outState.putStringArrayList("notesDates", notesDates);
        outState.putStringArrayList("notesTags", notesTags);
    }


    // Listeners
    public interface listenerDelFragment{
        void clickOnNote(int selectedNoteId);
    }
    private listenerDelFragment elListener;

    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            elListener = (listenerDelFragment) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("La clase " +context.toString()
                    + " debe implementar listenerDelFragment");
        }
    }

}
