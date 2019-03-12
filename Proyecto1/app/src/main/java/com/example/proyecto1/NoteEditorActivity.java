package com.example.proyecto1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.PersistableBundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto1.dialogs.AddTagEditor;
import com.example.proyecto1.dialogs.ConfimExit;
import com.example.proyecto1.dialogs.DeleteNoteDialog;
import com.example.proyecto1.dialogs.DeleteTextStyles;
import com.example.proyecto1.dialogs.InsertLinkEditor;
import com.example.proyecto1.dialogs.NewTag;
import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.MainToolbar;
import com.example.proyecto1.utilities.MyDB;
import com.example.proyecto1.utilities.SpanStyleHelper;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteEditorActivity extends MainToolbar implements DeleteTextStyles.ListenerDelDialogo, InsertLinkEditor.ListenerDelDialogo, AddTagEditor.ListenerDelDialogo, NewTag.ListenerDelDialogo {

    int choosenTagId = -1;
    String choosenTagName = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Change the topbar options
        getMenuInflater().inflate(R.menu.editor_toolbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor_activity);
        // load top toolbar
        loadToolbar();
        showBackButtonOption();
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
        // Show the dialog to so the user inputs the info
        DialogFragment confirmationDialog = new InsertLinkEditor();
        confirmationDialog.show(getSupportFragmentManager(), "insertLinkEditor");
    }

    /**
     * Append the url to the text content
     */
    public void yesInsertUrl(View textToShow, View inputLink){
        EditText textBody = findViewById(R.id.noteBody);
        int posIni = textBody.getSelectionStart();

        SpannableStringBuilder spannable = new SpannableStringBuilder(textBody.getText());
        String link =
                "<a href='" + ((TextView) inputLink).getText().toString() + "'>" + ((TextView) textToShow).getText().toString() +
                "</a>";
        SpannableStringBuilder formatedLink = (SpannableStringBuilder) Html.fromHtml(link);

        spannable.insert(posIni, formatedLink); // insert the link in the text
        textBody.setMovementMethod(LinkMovementMethod.getInstance());
        textBody.setText(spannable);
        textBody.setSelection(posIni);
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

    /**
     * The user confirms that wants to delete the text styles
     */
    public void yesDeleteTextStyles(){
        EditText textBody = findViewById(R.id.noteBody);

        SpannableStringBuilder spannable = new SpannableStringBuilder(textBody.getText());

        // get all the spans attached to the SpannedString
        Object[] spans = spannable.getSpans(0, spannable.length(), Object.class);

        for (Object span : spans) {
            if (span instanceof CharacterStyle && !span.toString().contains("URL"))
                spannable.removeSpan(span);
        }
        textBody.setText(spannable);
    }


    /**
     * The user wants to add a tag to the post
     * @param v - the element clicked
     */
    public void addTag(View v){
        // Show the dialog to select one
        DialogFragment dialog = new AddTagEditor();
        Bundle bl = new Bundle();
        bl.putInt("choosenTagId", choosenTagId);
        bl.putString("choosenTagName", choosenTagName);
        dialog.setArguments(bl);
        dialog.show(getSupportFragmentManager(), "addTagEditor");
    }

    /**
     * The user has selected a tag to add to the post
     * @param tagId - the id of the selected tag
     */
    public void addTagToPost(int tagId, String tagName){
        choosenTagId = tagId;
        choosenTagName = tagName;
        TextView a = findViewById(R.id.assignedTag);
        if (tagId == -1){
            // nothing selected, tag name not shown
            a.setVisibility(View.INVISIBLE);
        }else{
            // a tag has been selected, show it
            a.setVisibility(View.VISIBLE);
            a.setVisibility(View.VISIBLE);
            a.setText(tagName);
        }
    }

    /**
     * The user wants to create a new tag
     */
    public void createNewTag(){
        // open dialog
        DialogFragment dialog = new NewTag();
        dialog.show(getSupportFragmentManager(), "newTag");
    }

    /**
     * The user has entered a name for the new tag, save it for the current user
     * @param tagName - the choosen name
     */
    public void yesNewTag(String tagName){
        MyDB gestorDB = new MyDB(getApplicationContext(), "Notes", null, 1);
        gestorDB.addTag(Data.getMyData().getActiveUsername(), tagName);
    }

    /**
     * The user wants to save an edited note
     */
    @Override
    public void saveNote() {
        EditText titleElement = findViewById(R.id.titleInput);
        String title = titleElement.getText().toString();

        if (title.trim().isEmpty()){
            // The title is empty
            int tiempo = Toast.LENGTH_SHORT;
            Toast aviso = Toast.makeText(getApplicationContext(),
                    R.string.failSavingNote_titleEmpty, tiempo);
            aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
            aviso.show();
            return; // Exit method
        }

        EditText bodyElement = findViewById(R.id.noteBody);

        if (bodyElement.getText().toString().trim().isEmpty()){
            // The note body is empty
            int tiempo = Toast.LENGTH_SHORT;
            Toast aviso = Toast.makeText(getApplicationContext(), R.string.failSavingNote_bodyEmpty,
                    tiempo);
            aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
            aviso.show();
            return; // Exit method
        }

        SpannableString spannable = new SpannableString(bodyElement.getText());

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = timeStamp + "_" + ".html";
        String htmlContent = Html.toHtml(spannable);
        htmlContent = htmlContent.replace("<u>", "") // delete the tags added by the conversor
                                .replace("</u>", "")
                                .replace(" dir=\"ltr\"", "");
        Log.i("aqui", htmlContent);

        try {
            OutputStreamWriter fichero = new OutputStreamWriter(openFileOutput(fileName,
                    Context.MODE_PRIVATE));
            fichero.write(htmlContent);
            fichero.close();

            MyDB gestorDB = new MyDB(getApplicationContext(), "Notes", null, 1);
            gestorDB.insertNewNote(title, fileName, choosenTagId,
                    Data.getMyData().getActiveUsername());

            // Note created
            Intent i = new Intent();
            i.putExtra("fileName", fileName);
            setResult(RESULT_OK, i);
            finish();
        } catch (IOException e){
            // show toast if error saving the file
            int tiempo = Toast.LENGTH_SHORT;
            Toast aviso = Toast.makeText(getApplicationContext(), R.string.failSavingNote, tiempo);
            aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
            aviso.show();
        }
    }

    @Override
    public void onBackPressed() {
        // If the back button is pressed the user has to confirm they want to exit
        DialogFragment confirmationDialog = new ConfimExit();
        confirmationDialog.show(getSupportFragmentManager(), "goBack");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        choosenTagId = savedInstanceState.getInt("choosenTagId");
        choosenTagName = savedInstanceState.getString("choosenTagName");
        addTagToPost(choosenTagId, choosenTagName); // show the tag if there's one like before
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("choosenTagId", choosenTagId);
        outState.putString("choosenTagName", choosenTagName);
    }
}
