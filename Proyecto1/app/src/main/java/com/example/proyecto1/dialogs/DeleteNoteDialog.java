package com.example.proyecto1.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.proyecto1.R;
import com.example.proyecto1.SingleNoteActivity;

public class DeleteNoteDialog extends DialogFragment {

    ListenerDelDialogo miListener;

    public interface ListenerDelDialogo {
        void yesDeleteNote();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        miListener = (ListenerDelDialogo) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String deleteNote_title = getResources().getString(R.string.deleteNote_title);
        builder.setTitle(deleteNote_title);
        String deleteNote_body = getResources().getString(R.string.deleteNote_body);
        builder.setMessage(deleteNote_body);

        final String positiveButton = getResources().getString(R.string.deleteNote_positive);
        String negativeButton = getResources().getString(R.string.deleteNote_negative);

        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.yesDeleteNote();
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
