package com.example.proyecto1.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.proyecto1.R;
import com.example.proyecto1.cardview.ElAdaptadorRecycler;
import com.example.proyecto1.cardview.ElViewHolder;
import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.MyDB;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private RecyclerView notes;
    private ArrayList<ArrayList<String>> notesData;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

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

        Log.i("aqui", "loaded");
        // load notes
        String activeUser = Data.getMyData().getActiveUsername();
        MyDB gestorDB = new MyDB(getActivity(), "Notes", null, 1);
        notesData = gestorDB.getNotesDataByUser(activeUser);

        // titles, dates, tags
        final ElAdaptadorRecycler eladaptador = new ElAdaptadorRecycler(notesData.get(1),
                notesData.get(2), notesData.get(3), new boolean[notesData.get(1).size()]);

        // Add listeners
        eladaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = notes.getChildAdapterPosition(v);

                int noteIdOfPosition = Integer.valueOf(notesData.get(0).get(clickedPosition));

                elListener.clickOnNote(noteIdOfPosition);

            }
        });
        notes.setAdapter(eladaptador);
        LinearLayoutManager elLayoutLineal= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        notes.setLayoutManager(elLayoutLineal);
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
        for (int i=0;i<notesData.size();i++){
            notesData.get(i).add(noteData.get(i));
        }
        // notify the adapter that the data has changed
        notes.getAdapter().notifyItemInserted(notesData.get(0).size()-1);
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
