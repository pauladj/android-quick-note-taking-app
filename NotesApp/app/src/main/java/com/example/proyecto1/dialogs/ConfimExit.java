package com.example.proyecto1.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.proyecto1.NoteEditorActivity;
import com.example.proyecto1.R;

public class ConfimExit extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        return new AlertDialog.Builder(getActivity())
            .setMessage(R.string.confirmExit_title)
            .setCancelable(false)
            .setPositiveButton(R.string.confirmExit_yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    getActivity().finish();
                }
            })
            .setNegativeButton(R.string.confirmExit_no, null)
            .show();
    }

}
