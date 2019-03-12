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
    private int selectedItem = -1;
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

        // load notes
        String activeUser = Data.getMyData().getActiveUsername();
        MyDB gestorDB = new MyDB(getActivity(), "Notes", null, 1);
        notesData = gestorDB.getNotesDataByUser(activeUser);

        // titles, dates, tags
        ElAdaptadorRecycler eladaptador = new ElAdaptadorRecycler(notesData.get(1), notesData.get(2), notesData.get(3));

        // Add listeners
        eladaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = notes.getChildAdapterPosition(v);
                int noteIdOfPosition = Integer.valueOf(notesData.get(0).get(clickedPosition));
                // clean the previous selected item
                if (selectedItem != -1){
                    ElViewHolder viewHolder =
                            (ElViewHolder) notes.findViewHolderForAdapterPosition(selectedItem);
                    TextView title = viewHolder.noteTitle;
                    //TextView title = notes.getChildAt(selectedItem).findViewById(R.id.noteTitle);
                    title.setBackgroundColor(Color.TRANSPARENT);
                    title.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                selectedItem = clickedPosition; // set new selected item*/

                elListener.clickOnNote(noteIdOfPosition);

            }
        });
        notes.setAdapter(eladaptador);
        LinearLayoutManager elLayoutLineal= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        notes.setLayoutManager(elLayoutLineal);
    }

    /**
     * Show an element is selected by coloring the background
     */
    public void markAsSelected(){
        if (notes != null){
            ElViewHolder viewHolder =
                    (ElViewHolder) notes.findViewHolderForAdapterPosition(selectedItem);
            TextView title = viewHolder.noteTitle;
            //TextView title = notes.getChildAt(selectedItem).findViewById(R.id.noteTitle);
            title.setBackgroundResource(R.color.colorPrimaryDark);
            title.setTextColor(Color.WHITE);
        }
    }

    public void addNote(String fileName){
        MyDB gestorDB = new MyDB(getActivity(), "Notes", null, 1);
        ArrayList<String> noteData = gestorDB.getLastAddedNoteData(fileName);
        for (int i=0;i<notesData.size();i++){
            notesData.get(i).add(noteData.get(i));
        }
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
