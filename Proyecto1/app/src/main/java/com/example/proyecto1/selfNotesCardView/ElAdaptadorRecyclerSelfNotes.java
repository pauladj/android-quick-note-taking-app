package com.example.proyecto1.selfNotesCardView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto1.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ElAdaptadorRecyclerSelfNotes extends RecyclerView.Adapter <ElViewHolderSelfNote> implements View.OnClickListener{

    private ArrayList<String> noteMessages;
    private ArrayList<String> noteDates;
    private ArrayList<String> noteImages;

    private View.OnClickListener listener;

    public ElAdaptadorRecyclerSelfNotes(ArrayList<String> noteMessages, ArrayList<String> notesDates,
                                        ArrayList<String> noteImages){
        this.noteMessages = noteMessages;
        this.noteDates = notesDates;
        this.noteImages = noteImages;
    }

    @Override
    public int getItemViewType(int position) {
        if (this.noteImages.get(position) == null && this.noteMessages.get(position) != null) {
            // es texto no una imagen o la imagen no está en el sistema porque se ha producido un
            // error al descargarla
            return 0; // message
        } else {
            return 2; // image
        }
    }

    public ElViewHolderSelfNote onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layout;
        if (viewType == 0){
            layout = R.layout.note_sent;
        }else{
            layout = R.layout.note_sent_image;
        }
        View ellayoutdelafila= LayoutInflater.from(viewGroup.getContext()).inflate(layout,null);
        ellayoutdelafila.setOnClickListener(this); // Add listener
        ElViewHolderSelfNote evh = new ElViewHolderSelfNote(ellayoutdelafila);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ElViewHolderSelfNote elViewHolder, int i) {
        if (elViewHolder.getItemViewType() == 0){
            // message
            // solo texto
            Log.i("aquias--", noteMessages.get(i));
            elViewHolder.noteMessage.setText(noteMessages.get(i));
        }else{
            // solo imagen
            // se tiene el path de la imagen
            try{
                // mostrar en los imageviews imágenes pequeñas para que el recyclerview no se
                // "atasque" y vaya lento
                String[] name = noteImages.get(i).split(".jpg");
                String fileName = name[0] + "_small.jpg";
                File imgFile = new  File(fileName);
                elViewHolder.noteImage.setImageURI(Uri.fromFile(imgFile));

            }catch (Exception e){

            }
        }

        String time = noteDates.get(i).split(" ")[1];
        elViewHolder.noteDate.setText(time);
    }

    @Override
    public int getItemCount() {
        return noteDates.size();
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
