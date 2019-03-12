package com.example.proyecto1.cardview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.proyecto1.R;

import java.util.ArrayList;

public class ElAdaptadorRecycler extends RecyclerView.Adapter <ElViewHolder> implements View.OnClickListener{

    private ArrayList<String> notesTitles;
    private ArrayList<String> notesDates;
    private ArrayList<String> notesTags;

    private View.OnClickListener listener;

    public ElAdaptadorRecycler(ArrayList<String> notesTitles, ArrayList<String> notesDates,
                               ArrayList<String> notesTags){
        this.notesTitles = notesTitles;
        this.notesDates = notesDates;
        this.notesTags = notesTags;
    }

    public ElViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View ellayoutdelafila= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_cardview,null);
        ellayoutdelafila.setOnClickListener(this); // Add listener
        ElViewHolder evh = new ElViewHolder(ellayoutdelafila);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ElViewHolder elViewHolder, int i) {
        elViewHolder.noteTitle.setText(notesTitles.get(i));
        elViewHolder.noteDate.setText(notesDates.get(i));
        elViewHolder.noteTag.setText(notesTags.get(i));
        if (notesTags.get(i) == null){
            // if there's no tag don't show space for it
            elViewHolder.noteTag.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return notesTitles.size();
    }

    // Add listeners
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onClick(View view){
        if (listener != null){
            listener.onClick(view);
        }
    }

}
