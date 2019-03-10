package com.example.proyecto1;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto1.dialogs.DeleteNoteDialog;
import com.example.proyecto1.dialogs.DeleteTextStyles;
import com.example.proyecto1.utilities.MainToolbar;
import com.example.proyecto1.utilities.SpanStyleHelper;

import java.lang.reflect.Type;

public class NoteEditorActivity extends MainToolbar implements DeleteTextStyles.ListenerDelDialogo {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor_activity);
        // load top toolbar
        loadToolbar();

        // add listeners
        final EditText noteBody = findViewById(R.id.noteBody);
        noteBody.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    findViewById(R.id.boldText).setEnabled(false);
                    findViewById(R.id.italicText).setEnabled(false);
                    findViewById(R.id.linkText).setEnabled(false);
                    findViewById(R.id.formatText).setEnabled(false);
                }else{
                    // The user can only use these buttons in the note body
                    findViewById(R.id.boldText).setEnabled(true);
                    findViewById(R.id.italicText).setEnabled(true);
                    findViewById(R.id.linkText).setEnabled(true);
                    findViewById(R.id.formatText).setEnabled(true);
                }
            }
        });

        // Scroll available even with scrollview
        // https://stackoverflow.com/a/24428854/11002531
        noteBody.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (noteBody.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

    }


    /**
     * Check if the user has selected note body text
     * @return true if selected, false if no text has been selected
     */
    private boolean checkIfSelection(){
        EditText noteBody = findViewById(R.id.noteBody);
        if (noteBody.getSelectionEnd() != noteBody.getSelectionStart()){
            return true;
        }else{
            // toast with error
            int tiempo = Toast.LENGTH_SHORT;
            Toast aviso = Toast.makeText(getApplicationContext(), R.string.failBoldText,
                    tiempo);
            aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
            aviso.show();
            return false;
        }
    }

    /**
     * If the user wants bold font
     * @param v
     */
    public void boldClicked(View v){
        boolean selection = checkIfSelection();
        if (selection){
            EditText noteBody = findViewById(R.id.noteBody);
            int end = noteBody.getSelectionEnd();
            SpanStyleHelper spanStyleHelper = new SpanStyleHelper(noteBody);
            noteBody.setText(
                    spanStyleHelper.toggleBoldSelectedText()
            );
            noteBody.setSelection(end);
        }
    }

    /**
     * If the user wants italic font
     * @param v
     */
    public void italicClicked(View v){
        boolean selection = checkIfSelection();
        if (selection) {
            EditText noteBody = findViewById(R.id.noteBody);
            int end = noteBody.getSelectionEnd();

            SpanStyleHelper spanStyleHelper = new SpanStyleHelper(noteBody);
            noteBody.setText(
                    spanStyleHelper.toggleItalicSelectedText()
            );
            noteBody.setSelection(end);
        }
    }

    /**
     * The user wants to insert a link
     * @param v
     */
    public void insertUrl(View v){

    }

    /**
     * The user confirms that wants to delete the text styles
     */
    public void yesDeleteTextStyles(){
        EditText textBody = findViewById(R.id.noteBody);

        SpannableStringBuilder spannable = new SpannableStringBuilder(textBody.getText());

        // get all the spans attached to the SpannedString
        Object[] spans = spannable.getSpans(0, spannable.length(), Object.class);

        for (Object span : spans) {
            if (span instanceof CharacterStyle)
                spannable.removeSpan(span);
        }
        textBody.setText(spannable);
    }

    /**
     * The user wants to remove all the styles
     * @param v
     */
    public void formatText(View v){
        // Show the dialog to confirm
        DialogFragment confirmationDialog = new DeleteTextStyles();
        confirmationDialog.show(getSupportFragmentManager(), "deleteTextStyles");
    }
}
