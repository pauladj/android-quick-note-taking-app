package com.example.proyecto1.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.widget.Toast;

import com.example.proyecto1.Common;
import com.example.proyecto1.LogInActivity;
import com.example.proyecto1.MainActivity;
import com.example.proyecto1.NotesToSelf;
import com.example.proyecto1.R;
import com.example.proyecto1.SignUpActivity;
import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.GeneradorConexionesSeguras;
import com.example.proyecto1.utilities.MyDB;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;


public class AsyncTaskFragment extends Fragment {

    private ProgressDialog progressDialog;
    private boolean isTaskRunning = false;
    private String action; // action to do, signup, login....
    private boolean success = false; // if the asynctask has been successful...

    private int currentProgress; // progreso actual
    private int totalProgress; // progreso máx al que se puede llegar

    private String message; // el contenido del self message
    private String image; //el path de la imagen del self message
    private String date; // la fecha del self message


    public interface TaskCallbacks {
        void showToast(Boolean acrossWindows, int messageId);
        void addSelfNoteToRecycler(String message, String imagePath, String date);
    }

    private TaskCallbacks mCallbacks;
    private DummyTask mTask;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    /**
     * Solo se llamará una vez
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mantener el fragmento aunque se rote la pantalla.
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Si la pantalla se ha girado y la tarea todavía se está ejecutando mostrar otra vez el
        // progressdialog
        if (isTaskRunning) {
            // Se configura el progress dialog
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(false);
            progressDialog.setTitle(getResources().getString(R.string.loadingTitle));
            progressDialog.setMessage(getResources().getString(R.string.loadingBody));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(currentProgress);
            progressDialog.setMax(totalProgress);
            progressDialog.show();
        }
    }

    /**
     * Start the task
     */
    public void start(String[] params){
        // Create and execute the background task.
        mTask = new DummyTask();
        Log.i("aquiw", "asdf22222222222");
        mTask.execute(params);
    }

    /**
     * Set the action to execute (login,..)
     */
    public void setAction(String action){
        this.action = action;
    }


    @Override
    public void onDetach() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDetach();
        mCallbacks = null;
    }


    private class DummyTask extends AsyncTask<String, Integer, Pair<Boolean, Integer>> {

        @Override
        public void onPreExecute() {
            isTaskRunning = true;
            Log.i("aquiw", "333");

            // configurar y enseñar el diálogo
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(1); // por defecto una acción
            progressDialog.setTitle(getResources().getString(R.string.loadingTitle));
            progressDialog.setMessage(getResources().getString(R.string.loadingBody));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }

        @Override
        public void onPostExecute(Pair<Boolean, Integer> result) {
            if (progressDialog != null) {
                // cerrar el diálogo
                progressDialog.dismiss();
            }
            isTaskRunning = false;
            if (result != null){
                // si hay mensaje toast enviarlo a la actividad padre
                mCallbacks.showToast(result.first, result.second);
            }
            if (action.equals("signup") && success){
                // go to the login view
                SignUpActivity activity = (SignUpActivity)getActivity();
                if (activity != null){
                    activity.goToLogIn();
                }
            }else if(action.equals("login") && success){
                // go to the main activity
                LogInActivity activity = (LogInActivity)getActivity();
                if (activity != null) {
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
            }else if(action.equals("fetchselfnotes")){
                // cargar página noteToSelf
                Intent i = new Intent(getActivity(), NotesToSelf.class);
                startActivity(i);
            }else if((action.equals("sendselfnotes") || action.equals("sendphoto")) && success){
                // la nota de solo texto se ha enviado correctamente
                mCallbacks.addSelfNoteToRecycler(message, image, date);
            }else if(action.equals("logout") && success){
                // el usuario quiere salir de la cuenta
                Data.getMyData().setActiveUsername(null); // remove active user

                // borrarlo de las preferencias
                SharedPreferences prefs_especiales= getActivity().getSharedPreferences(
                        "preferencias_especiales",
                        Context.MODE_PRIVATE);

                SharedPreferences.Editor editor2= prefs_especiales.edit();
                editor2.remove("activeUsername");
                editor2.apply();

                // start login screen
                Intent i = new Intent(getActivity(), LogInActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);// Limpiar pila de actividades
                startActivity(i);
                getActivity().finish();
            }
        }


        @Override
        protected Pair<Boolean, Integer> doInBackground(String... strings) {
            Log.i("aqui", "backgro");
            Log.i("aqui", action);
            try {
                // se forma la url con sus opciones para pedir datos al servidor
                String direccion = "https://134.209.235.115/pdejaime001/WEB/urls.php";

                HttpsURLConnection urlConnection=
                        GeneradorConexionesSeguras.getInstance().crearConexionSegura(getActivity(),direccion);

                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                // se forma el json y se procesa la respuesta según el método que se quiera

                JSONObject parametrosJSON = new JSONObject();
                parametrosJSON.put("action", action);
                if (action == "signup") {
                    // El usuario quiere registrarse
                    parametrosJSON.put("username", strings[0]);
                    parametrosJSON.put("password", strings[1]);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    parametrosJSON.put("accessToken", timestamp.getTime());

                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(parametrosJSON.toString());
                    out.close();

                    JSONObject json = getJsonFromResponse(urlConnection);

                    // if ok
                    if (json.containsKey("success")){
                        // toast
                        success = true;
                        currentProgress = 1;
                        publishProgress(currentProgress); // avisar del progreso
                        return Pair.create(true, R.string.userSuccessfullyRegistered);
                    }else {
                        String error = json.get("error").toString();
                        if (error.equals("username_exists")){
                            // show toast
                            return Pair.create(true, R.string.userAlreadyExists);
                        }else{
                            throw new Exception("connection_error");
                        }
                    }
                }else if (action == "login") {
                    // El usuario quiere registrarse
                    parametrosJSON.put("username", strings[0]);
                    parametrosJSON.put("password", strings[1]);
                    parametrosJSON.put("firebaseToken", strings[2]);

                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(parametrosJSON.toString());
                    out.close();

                    JSONObject json = getJsonFromResponse(urlConnection);

                    // if ok
                    if (json.containsKey("success")) {
                        // toast
                        // set the active username
                        Data.getMyData().setActiveUsername(json.get("success").toString());

                        // guardar el usuario activo en las preferencias
                        LogInActivity activity = (LogInActivity) getActivity();
                        if (activity != null) {
                            activity.setActiveUsername(json.get("success").toString());
                        }

                        Log.i("aqui_active", Data.getMyData().getActiveUsername());
                        success = true;

                        currentProgress = 1;
                        publishProgress(currentProgress); // avisar
                    } else {
                        String error = json.get("error").toString();
                        if (error.equals("wrong_credentials")) {
                            // show toast
                            return Pair.create(true, R.string.incorrectPassword);
                        } else {
                            throw new Exception("connection_error");
                        }
                    }
                }else if(action == "logout"){
                    // El usuario quiere salir de su cuenta
                    parametrosJSON.put("username", strings[0]);
                    parametrosJSON.put("firebaseToken", strings[1]);

                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(parametrosJSON.toString());
                    out.close();

                    JSONObject json = getJsonFromResponse(urlConnection);

                    // if not ok
                    if (!json.containsKey("success")){
                        throw new Exception("connection_error");
                    }
                    success = true;
                }else if(action == "fetchselfnotes"){
                    // se obtienen notas nuevas, es decir, si el usuario no tiene datos de
                    // aplicación se obtendrán todas, pero si ha realizado esta acción hace 5
                    // minutos se obtendrán las nuevas

                    // se obtiene el json

                    // obtener la fecha de la última vez que se realizó esta acción
                    String pattern = "yyyy-MM-dd HH:mm:ss";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                    String currentDate = simpleDateFormat.format(new Date());
                    Log.i("aqui_currentdate", currentDate);

                    SharedPreferences prefs_especiales= getActivity().getSharedPreferences(
                            "preferencias_especiales",
                            Context.MODE_PRIVATE);
                    String lastFetchedDate = prefs_especiales.getString("lastFetchedDate",
                            currentDate);


                    parametrosJSON.put("username", Data.getMyData().getActiveUsername());
                    parametrosJSON.put("date", lastFetchedDate);
                    parametrosJSON.put("now", currentDate);
                    Log.i("aqui_envia", parametrosJSON.toJSONString() );
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(parametrosJSON.toString());
                    out.close();

                    JSONObject json = getJsonFromResponse(urlConnection);

                    // si se ha producido un problema
                    if (!json.containsKey("success")){
                        throw new Exception("connection_error");
                    }

                    // se recorren todas las selfnotes no vistas hasta ahora
                    JSONArray selfNotes = (JSONArray) json.get("success");

                    // se empieza una transacción, si se produce un fallo se hará rollback de todo
                    MyDB gestorDB = new MyDB(getActivity(), "Notes", null, 1);
                    SQLiteDatabase db = gestorDB.getWritableDatabase();
                    db.beginTransactionNonExclusive();
                    totalProgress = selfNotes.size(); // el total al que habrá que llegar para
                                                      // completar la tarea asíncrona
                    for (int i = 0; i < selfNotes.size(); i++) {
                        JSONObject row = (JSONObject) selfNotes.get(i);

                        Log.i("aqui_iteracion", row.toJSONString());

                        String imagePath = (String) row.get("imagePath");
                        String imageLocalPath = "";
                        String date = (String) row.get("date");
                        String message = (String) row.get("message");


                        if (imagePath != null){
                            // la nota es una imagen, se descarga y se guarda
                            imageLocalPath = downloadAndSaveImage(imagePath);
                            if (imageLocalPath.equals("")){
                                // se ha producido un error al descargar/guardar la imagen
                                message = getResources().getString(R.string.imageError);
                            }
                        }
                        // se guarda en la base de datos
                        String activeUser = Data.getMyData().getActiveUsername();
                        gestorDB.addSelfNote(activeUser, message, imageLocalPath, date, db);
                        currentProgress = i;
                        publishProgress(currentProgress); // actualizar la ventana para mostrar
                                                          // progreso
                    }

                    // si all es correcto y se llega aquí
                    db.setTransactionSuccessful();
                    db.endTransaction();

                    // guardar fecha y hora del último fetch si no ha habido ningún fallo
                    SharedPreferences.Editor editor2= prefs_especiales.edit();
                    editor2.putString("lastFetchedDate", currentDate);
                    editor2.apply();

                    db.close(); // se cierra la base de datos
                }else if(action == "sendselfnotes"){
                    // send a self note

                    // obtener fecha actual
                    String pattern = "yyyy-MM-dd HH:mm:ss";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    String currentDate = simpleDateFormat.format(new Date());
                    Log.i("aqui_currentdate", currentDate);

                    // añadir parámetros
                    parametrosJSON.put("username", Data.getMyData().getActiveUsername());
                    parametrosJSON.put("date", currentDate);
                    parametrosJSON.put("message", strings[0]);
                    Log.i("aqui_envia", parametrosJSON.toJSONString() );
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(parametrosJSON.toString());
                    out.close();

                    JSONObject json = getJsonFromResponse(urlConnection);

                    // si se ha producido un problema
                    if (!json.containsKey("success")){
                        return Pair.create(true, R.string.errorSendingMessage);
                    }

                    // se guarda en la base de datos
                    String activeUser = Data.getMyData().getActiveUsername();
                    MyDB gestorDB = new MyDB(getActivity(), "Notes", null, 1);
                    gestorDB.addSelfNote(activeUser, strings[0], null, currentDate, null);

                    // se cambia la hora del último fetch, porque si algún otro dispositivo envía
                    // un mensaje se captura con firebase y se añade manualmente
                    SharedPreferences prefs_especiales= getActivity().getSharedPreferences(
                            "preferencias_especiales",
                            Context.MODE_PRIVATE);

                    // guardar fecha y hora del último fetch
                    SharedPreferences.Editor editor2= prefs_especiales.edit();
                    editor2.putString("lastFetchedDate", currentDate);
                    editor2.apply();

                    success = true;
                    message = strings[0];
                    image = null;
                    date = currentDate;

                    currentProgress = 1;
                    publishProgress(currentProgress); // avisar del progreso
                }else if(action == "sendphoto"){
                    // Mandar una imagen tomada por la cámara de fotos

                    // obtener, comprimir y transformar la imagen a Base64
                    String uri = strings[0];
                    Uri imagen = Uri.fromFile(new File(uri));

                    Bitmap mBitmap =
                            MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                            imagen);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                    byte[] fototransformada = stream.toByteArray();
                    String fotoen64 = Base64.encodeToString(fototransformada,Base64.DEFAULT);

                    // obtener fecha de ahora
                    String pattern = "yyyy-MM-dd HH:mm:ss";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    String currentDate = simpleDateFormat.format(new Date());

                    // formar parámetros a enviar
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("action", action)
                            .appendQueryParameter("username",  Data.getMyData().getActiveUsername())
                            .appendQueryParameter("date", currentDate)
                            .appendQueryParameter("image", fotoen64);
                    String parametrosURL = builder.build().getEncodedQuery();

                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(parametrosURL);
                    out.close();

                    Log.i("aquiee", parametrosJSON.toString());

                    JSONObject json = getJsonFromResponse(urlConnection);

                    // si se ha producido un problema
                    if (!json.containsKey("success")){
                        throw new Exception("connection_error");
                    }

                    // se guarda en la base de datos
                    String activeUser = Data.getMyData().getActiveUsername();
                    MyDB gestorDB = new MyDB(getActivity(), "Notes", null, 1);
                    gestorDB.addSelfNote(activeUser, null, uri, currentDate, null);

                    // se cambia la hora del último fetch, porque si algún otro dispositivo envía
                    // un mensaje se captura con firebase y se añade manualmente
                    SharedPreferences prefs_especiales= getActivity().getSharedPreferences(
                            "preferencias_especiales",
                            Context.MODE_PRIVATE);

                    // guardar fecha y hora del último fetch
                    SharedPreferences.Editor editor2= prefs_especiales.edit();
                    editor2.putString("lastFetchedDate", currentDate);
                    editor2.apply();

                    success = true;
                    message = null;
                    image = uri;
                    date = currentDate;

                    currentProgress = 1;
                    publishProgress(currentProgress); // avisar
                }
            } catch (Exception e) {
                // error
                // toast of error
                Log.i("aquidf", e.getMessage());
                return Pair.create(true, R.string.serverError);
            }
            return null;
        }

        /**
         * Si se notifica un avance en la tarea
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(currentProgress);
            progressDialog.setMax(totalProgress);

        }

        /**
         * Download an image from the server and save it on the phone
         * @param url - the url of the image to download
         * @return - the local path of the image
         */
        private String downloadAndSaveImage(String url){
            try {
                Log.i("aqui_downloadaimage", "start");

                HttpsURLConnection conn =
                        GeneradorConexionesSeguras.getInstance().crearConexionSegura(getActivity(), url);

                int responseCode = 0;

                responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    // se guarda la foto de tamaño normal
                    Bitmap elBitmap = BitmapFactory.decodeStream(conn.getInputStream());

                    File eldirectorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String fileName = timeStamp + "_";

                    File imagenFich = new File(eldirectorio, fileName + ".jpg");
                    OutputStream os = new FileOutputStream(imagenFich);

                    elBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();

                    // se guarda la foto en miniatura para que el recyclerview no sea lento
                    int anchoImagen = elBitmap.getWidth();
                    int altoImagen = elBitmap.getHeight();

                    // redimensionar
                    altoImagen = ((altoImagen * 240) / anchoImagen);
                    elBitmap  = Bitmap.createScaledBitmap(elBitmap, 240, altoImagen, true);

                    File imagenFichSmall = new File(eldirectorio, fileName + "_small.jpg");
                    os = new FileOutputStream(imagenFichSmall);

                    elBitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
                    os.flush();
                    os.close();

                    // avisar a la galería de que hay una nueva foto
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(imagenFich);
                    mediaScanIntent.setData(contentUri);
                    getActivity().sendBroadcast(mediaScanIntent);


                    Log.i("aqui_downloadaimage", "end");
                    Log.i("aquiu_path", imagenFich.getPath());
                    return imagenFich.getPath(); // devolver el path local de la imagen

                }else{
                    throw new Exception();
                }
            } catch (Exception e) {
                // error downloading image
                Log.i("aquinoooo", e.getMessage());
                return "";
            }
        }

        /**
         * We get the json from the http request
         * @param urlConnection
         * @return - the json object of the response
         * @throws Exception - if there is a connection error
         */
        private JSONObject getJsonFromResponse(HttpURLConnection urlConnection) throws Exception{
            try{
                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {
                    BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String line, result = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    inputStream.close();
                    Log.i("aqui", result);

                    // get the json of the response
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(result);
                    Log.i("aquijson", json.toString());
                    return json;
                }else{
                    Log.i("aqui", String.valueOf(statusCode));
                    throw new Exception("connection_error");
                }
            }catch (Exception e){
                // error
                // there was a connection error
                if (urlConnection != null){
                    // cerrar conexión
                    urlConnection.disconnect();
                }
                throw new Exception("connection_error");
            }

        }

    }
}