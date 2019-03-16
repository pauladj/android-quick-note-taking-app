package com.example.proyecto1.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto1.R;
import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.MyDB;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddRemoveTag extends DialogFragment {

    ListenerDelDialogo miListener;

    private ArrayList<Integer> loselegidos = new ArrayList<>(); // chosen tags to be deleted

    public interface ListenerDelDialogo {
        void yesRemoveTags(ArrayList<Integer> tagsId);
        void createNewTag();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey("loselegidos")){ // si la actividad no se crea por
                // primera vez se recuperan las etiquetas ya elegidas anteriormente
                loselegidos = savedInstanceState.getIntegerArrayList("loselegidos");
            }
        }

        miListener = (ListenerDelDialogo) getActivity();

        // el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.addRemoveTag_title));


        MyDB gestorDB = new MyDB(getActivity(), "Notes", null, 1);
        // se obtienen las etiquetas del usuario
        final ArrayList<ArrayList<String>> tags =
                gestorDB.getTagsByUser(Data.getMyData().getActiveUsername());
        if (tags == null){
            // database error
            int tiempo = Toast.LENGTH_SHORT;
            Toast aviso = Toast.makeText(getActivity(), R.string.databaseError, tiempo);
            aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
            aviso.show();
        }else{
            // configure dialog
            CharSequence[] tagNames = tags.get(1).toArray(new CharSequence[tags.get(1).size()]);

            builder.setMultiChoiceItems(tagNames, null,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            int tagId = Integer.valueOf(tags.get(0).get(which));
                            if (isChecked){
                                loselegidos.add(tagId); // add tag id
                            }
                            else if (loselegidos.contains(tagId)){
                                int index = loselegidos.indexOf(tagId); // search the index of the tag id
                                loselegidos.remove(index); // delete that element from array
                            }
                        }
                    });
        }

        final String positiveButton = getResources().getString(R.string.addRemoveTag_remove);
        String negativeButton = getResources().getString(R.string.insertLink_cancel);
        String neutralButton = getResources().getString(R.string.addTag_newTag);

        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                miListener.yesRemoveTags(loselegidos); // call the method to remove tags
            }
        });

        builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });

        builder.setNeutralButton(neutralButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // create new tag
                miListener.createNewTag();
            }
        });

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("loselegidos", loselegidos); // save the selected ones
    }
}
