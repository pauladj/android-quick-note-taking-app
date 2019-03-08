package com.example.proyecto1.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyecto1.R;

import org.w3c.dom.Text;

public class SingleNoteFragment extends Fragment {

    private View myFragmentView;
    private TextView laetiq;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        myFragmentView = inflater.inflate(R.layout.single_note_fragment, parent, false);
        laetiq = myFragmentView.findViewById(R.id.textView);
        Log.i("aqui", "restaurando");
        return myFragmentView;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * Loads a note information knowing its id
     * @param noteId - the id of the note
     */
    public void loadNote(int noteId){
        laetiq.setText(String.valueOf(noteId));
    }

}