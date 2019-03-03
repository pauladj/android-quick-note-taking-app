package com.example.proyecto1.cardview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto1.R;

public class ElAdaptadorRecycler extends RecyclerView.Adapter <ElViewHolder> {

    private String[] notesTitles;
    private String[] notesDates;
    private String[] notesTags;

    public ElAdaptadorRecycler(String[] notesTitles, String[] notesDates, String[] notesTags){
        this.notesTitles = notesTitles;
        this.notesDates = notesDates;
        this.notesTags = notesTags;
    }

    public ElViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View ellayoutdelafila= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_cardview,null);
        ElViewHolder evh = new ElViewHolder(ellayoutdelafila);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ElViewHolder elViewHolder, int i) {
        elViewHolder.noteTitle.setText(notesTitles[i]);
        elViewHolder.noteDate.setText(notesDates[i]);
        elViewHolder.noteTag.setText(notesTags[i]);
    }

    @Override
    public int getItemCount() {
        return notesTitles.length;
    }
}
