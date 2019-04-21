package com.example.proyecto1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto1.cardview.ElAdaptadorRecycler;
import com.example.proyecto1.selfNotesCardView.ElAdaptadorRecyclerSelfNotes;
import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.MainToolbar;
import com.example.proyecto1.utilities.MyDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class NotesToSelf extends MainToolbar {

    private String uri; // si se quiere sacar una foto aquí se guardará su localización
    private String photoName; // nombre de la foto que se quiere sacar con la cámara

    private ArrayList<String> messagesText; // array of the text of the self messages
    private ArrayList<String> messagesImages; // array of the path of the images of the self messages
    private ArrayList<String> messagesDates; // array of the dates of the self messages

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("uri", uri);
        outState.putString("photoName", photoName);
        outState.putStringArrayList("messagesText", messagesText);
        outState.putStringArrayList("messagesDates", messagesDates);
        outState.putStringArrayList("messagesImages", messagesImages);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        uri = savedInstanceState.getString("uri");
        photoName = savedInstanceState.getString("photoName");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Change the topbar options
        getMenuInflater().inflate(R.menu.self_messages, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_to_self);
        // load top toolbar
        loadToolbar();
        showBackButtonOption();

        // load recycler view
        RecyclerView layoutRecycler = findViewById(R.id.notesToSelf);

        ArrayList<ArrayList<String>> notesData = null;

        // load self notes
        if (savedInstanceState != null && savedInstanceState.containsKey("messagesDates")){
            // la pantalla se ha girado o ya existía la actividad
            messagesImages = savedInstanceState.getStringArrayList("messagesImages");
            messagesDates = savedInstanceState.getStringArrayList("messagesDates");
            messagesText = savedInstanceState.getStringArrayList("messagesText");
        }else{
            // la actividad se inicia por primera vez
            String activeUser = Data.getMyData().getActiveUsername();
            MyDB gestorDB = new MyDB(this, "Notes", null, 1);
            notesData = gestorDB.getSelfNotesByUser(activeUser); //
            // text, date, image
        }

        if (notesData == null && messagesText == null){
            // database error cuando la actividad se inicia por primera vez
            int tiempo = Toast.LENGTH_SHORT;
            Toast aviso = Toast.makeText(this, R.string.databaseError, tiempo);
            aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
            aviso.show();
        }else {
            if (messagesDates == null){
                // primera vez que se carga la actividad
                messagesText = notesData.get(0);
                messagesDates = notesData.get(1);
                messagesImages = notesData.get(2);
            }

            final ElAdaptadorRecyclerSelfNotes eladaptador =
                    new ElAdaptadorRecyclerSelfNotes(messagesText, messagesDates, messagesImages);

            // Add listeners
            eladaptador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = layoutRecycler.getChildAdapterPosition(v);

                    String imagePath = messagesImages.get(clickedPosition);

                    clickOnSelfNote(imagePath);

                }
            });
            layoutRecycler.setAdapter(eladaptador);
            LinearLayoutManager elLayoutLineal = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            layoutRecycler.setLayoutManager(elLayoutLineal);
            layoutRecycler.smoothScrollToPosition(messagesDates.size());
            eladaptador.notifyDataSetChanged();
        }
    }

    /**
     * A self note has been clicked
     * @param imagePath - the path of the image
     */
    private void clickOnSelfNote(String imagePath){
        if (imagePath != null && !imagePath.isEmpty()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(imagePath), "image/*");
            startActivity(intent);
        }
    }

    /**
     * The user wants to take a photo with the camera
     */
    public void tryTakingPhotoWithTheCamera(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED) {
            //EL PERMISO NO ESTÁ CONCEDIDO, PEDIRLO
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                // MOSTRAR AL USUARIO UNA EXPLICACIÓN DE POR QUÉ ES NECESARIO EL PERMISO

            }
            else{
                //EL PERMISO NO ESTÁ CONCEDIDO TODAVÍA O EL USUARIO HA INDICADO
                //QUE NO QUIERE QUE SE LE VUELVA A SOLICITAR

            }
            //PEDIR EL PERMISO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    200);
        }
        else {
            //EL PERMISO ESTÁ CONCEDIDO, EJECUTAR LA FUNCIONALIDAD
            takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200:{
                // Si la petición se cancela, granResults estará vacío
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISO CONCEDIDO, EJECUTAR LA FUNCIONALIDAD
                    takePhoto();
                }
                else {
                // PERMISO DENEGADO, DESHABILITAR LA FUNCIONALIDAD O EJECUTAR ALTERNATIVA

                }
                return;
            }
        }
    }

    /**
     * El usuario tiene permisos para sacar una foto
     */
    private void takePhoto(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = timeStamp + "_";

        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File fichImg = null;
        Uri uriimagen = null;
        try {
            fichImg = File.createTempFile(fileName, ".jpg", directorio);
            uriimagen = FileProvider.getUriForFile(this, "com.example.proyecto1.provider",
                    fichImg);

            uri = fichImg.getPath();
            photoName = uri.split(".jpg")[0];
            Intent elIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            elIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriimagen);
            startActivityForResult(elIntent, 100);
        } catch (Exception e) {
            Log.i("aqui", e.getMessage());
            showToast(false, R.string.errorTakingPicture);
        }
    }

    /**
     * Response
     * @param requestCode - to specify this is the callback of capturing an image
     * @param resultCode - the result code of capturing an image
     * @param data - the data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // se guarda la foto en miniatura si su ancho es mayor de 240
            Uri contentUri = Uri.fromFile(new File(uri));
            try {
                Bitmap elBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                        contentUri);

                int anchoImagen = elBitmap.getWidth();
                int altoImagen = elBitmap.getHeight();

                // redimensionar y guardar una miniatura de la imagen para que el recyclerview no
                // vaya lento
                altoImagen = ((altoImagen * 240) / anchoImagen);
                elBitmap  = Bitmap.createScaledBitmap(elBitmap, 240, altoImagen, true);

                File eldirectorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File imagenFich = new File(photoName + "_small.jpg");
                OutputStream os = new FileOutputStream(imagenFich);

                elBitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
                os.flush();
                os.close();

                // mandar aviso a la galería de que se ha añadido una imagen
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                // mandar foto al servidor
                String[] params = {uri};
                getmTaskFragment().setAction("sendphoto");
                getmTaskFragment().start(params);
            }catch (Exception e){
                showToast(false, R.string.errorTakingPicture);
            }


        }
    }

    /**
     * The user wants to send a note
     * @param v
     */
    public void sendSelfNote(View v){
        EditText noteMessageContainer = findViewById(R.id.edittext_chat);
        String noteMessage = noteMessageContainer.getText().toString();
        if (noteMessage.isEmpty()){
            // si el mensaje está vacío mostrar toast y no enviar
            showToast(false, R.string.failSavingNote_bodyEmpty);
            return;
        }
        noteMessageContainer.setText("");
        String[] params = {noteMessage};
        getmTaskFragment().setAction("sendselfnotes");
        getmTaskFragment().start(params);
    }

    /**
     * Add a self note to the recycler view
     * @param message - the text of the message
     * @param imagePath - the image path
     * @param date - the date of the message
     */
    public void addSelfNoteToRecycler(String message, String imagePath, String date){
        messagesText.add(message);
        messagesDates.add(date);
        messagesImages.add(imagePath);
        RecyclerView recycler = findViewById(R.id.notesToSelf);
        recycler.getAdapter().notifyItemInserted(messagesDates.size()-1);
        recycler.smoothScrollToPosition(messagesDates.size());

    }
}
