package com.example.proyecto1.selfNotesCardView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyecto1.R;

public class ElViewHolderSelfNote extends RecyclerView.ViewHolder {

    public TextView noteMessage;
    public TextView noteDate;
    public ImageView noteImage;

    public ElViewHolderSelfNote(View v) {
        super(v);
        if (v.findViewById(R.id.text_image) == null){
            // solo texto
            noteMessage = v.findViewById(R.id.text_message_body);
            noteImage = null;
        }else{
            // solo imagen
            noteImage = v.findViewById(R.id.text_image);
            noteMessage = null;
        }
        noteDate = v.findViewById(R.id.text_message_time);
    }
}
