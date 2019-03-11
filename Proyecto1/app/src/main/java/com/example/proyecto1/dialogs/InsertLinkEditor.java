package com.example.proyecto1.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.example.proyecto1.R;

public class InsertLinkEditor extends DialogFragment {

    ListenerDelDialogo miListener;

    public interface ListenerDelDialogo {
        void yesInsertUrl(View textToShow, View inputLink);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        miListener = (ListenerDelDialogo) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.insertLink_title));
        LayoutInflater inflater=getActivity().getLayoutInflater();
        final View elaspecto= inflater.inflate(R.layout.insert_url_dialog,null);
        builder.setView(elaspecto);

        final String positiveButton = getResources().getString(R.string.insertLink_save);
        String negativeButton = getResources().getString(R.string.insertLink_cancel);

        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.yesInsertUrl(elaspecto.findViewById(R.id.inputTextToShow),
                        elaspecto.findViewById(R.id.inputLink));
            }
        });

        builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });

        return builder.create();
    }
}
