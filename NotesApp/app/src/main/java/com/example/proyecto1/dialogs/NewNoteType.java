package com.example.proyecto1.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.proyecto1.MainActivity;
import com.example.proyecto1.NoteEditorActivity;
import com.example.proyecto1.R;

public class NewNoteType extends DialogFragment {

    ListenerNewNoteType miListener;

    public interface ListenerNewNoteType {
        void createNormalNote();
        void createSelfNote();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        miListener = (ListenerNewNoteType) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose);
        CharSequence[] opciones = {getResources().getString(R.string.chooseNote),
                getResources().getString(R.string.chooseQuickNote)};
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    miListener.createNormalNote();
                }else{
                    miListener.createSelfNote();
                }
            }
        });

        return builder.create();
    }
}