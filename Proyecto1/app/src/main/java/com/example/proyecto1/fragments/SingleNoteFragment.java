package com.example.proyecto1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.proyecto1.R;
import com.example.proyecto1.utilities.MyDB;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class SingleNoteFragment extends Fragment {

    private WebView noteContent;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View myFragmentView = inflater.inflate(R.layout.single_note_fragment, parent, false);
        noteContent = myFragmentView.findViewById(R.id.noteContent);
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
        MyDB gestorDB = new MyDB(getActivity().getApplicationContext(), "Notes", null, 1);
        // the filename where the content of the note is
        String noteFileName = gestorDB.getNoteFileName(noteId);
        try {
            if (noteFileName == null){
                throw new FileNotFoundException();
            }

            BufferedReader ficherointerno = new BufferedReader(new InputStreamReader(
                    getActivity().openFileInput(noteFileName)));
            String content = ficherointerno.readLine();
            ficherointerno.close();

            noteContent.loadData(content, "text/html; charset=UTF-8", null);
        }catch (Exception e){
            noteContent.loadData("File not found","text/html; charset=UTF-8", null);
        }

    }

}