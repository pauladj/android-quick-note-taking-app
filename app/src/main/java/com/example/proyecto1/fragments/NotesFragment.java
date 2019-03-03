package com.example.proyecto1.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto1.R;
import com.example.proyecto1.cardview.ElAdaptadorRecycler;

public class NotesFragment extends Fragment {

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.notes_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onActivityCreated(savedInstanceState);

        // load recycler view
        RecyclerView notes = getView().findViewById(R.id.reciclerView);

        String[] nombres={"Bart Simpson","Edna Krabappel", "Homer Simpson", "Lisa Simpson",
                "Seymour Skinner", "Bart Simpson","Edna Krabappel", "Homer Simpson", "Lisa Simpson",
                "Seymour Skinner","Bart Simpson","Edna Krabappel", "Homer Simpson", "Lisa Simpson",
                "Seymour Skinner","Bart Simpson","Edna Krabappel", "Homer Simpson", "Lisa Simpson",
                "Seymour Skinner"};
        ElAdaptadorRecycler eladaptador = new ElAdaptadorRecycler(nombres,nombres, nombres);
        notes.setAdapter(eladaptador);

        LinearLayoutManager elLayoutLineal= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        notes.setLayoutManager(elLayoutLineal);
    }
}
