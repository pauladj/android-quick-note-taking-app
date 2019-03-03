package com.example.proyecto1.cardview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.proyecto1.R;

public class ElViewHolder extends RecyclerView.ViewHolder {

    public TextView noteTitle;
    public TextView noteDate;
    public TextView noteTag;

    public ElViewHolder(View v){
        super(v);
        noteTitle = v.findViewById(R.id.noteTitle);
        noteDate = v.findViewById(R.id.noteDate);
        noteTag = v.findViewById(R.id.noteTag);
    }
}
