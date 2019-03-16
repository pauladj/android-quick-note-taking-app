package com.example.proyecto1.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto1.R;

public class NewTag extends DialogFragment {

    ListenerDelDialogo miListener;

    public interface ListenerDelDialogo {
        void yesNewTag(String nameOfTag);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        miListener = (ListenerDelDialogo) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.newTag_title));
        LayoutInflater inflater=getActivity().getLayoutInflater();
        final View elaspecto = inflater.inflate(R.layout.new_tag_dialog,null);
        builder.setView(elaspecto);

        final String positiveButton = getResources().getString(R.string.insertLink_save);
        String negativeButton = getResources().getString(R.string.insertLink_cancel);

        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView tagNameInput = elaspecto.findViewById(R.id.inputTextTag);
                String text = tagNameInput.getText().toString();
                if (text.trim().isEmpty()){
                    // tag name empty
                    int tiempo = Toast.LENGTH_SHORT;
                    Toast aviso = Toast.makeText(getActivity(), R.string.emptyTag, tiempo);
                    aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
                    aviso.show();
                }else{
                    miListener.yesNewTag(tagNameInput.getText().toString());
                }
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
