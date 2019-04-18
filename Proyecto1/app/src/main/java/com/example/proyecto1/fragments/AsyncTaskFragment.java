package com.example.proyecto1.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.io.File;
import java.io.FileOutputStream;
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

/**
 * This Fragment manages a single background task and retains
 * itself across configuration changes.
 */
public class AsyncTaskFragment extends Fragment {

    private ProgressDialog progressDialog;
    private boolean isTaskRunning = false;
    private String action; // action to do, signup, login....
    private boolean success = false; // if the asynctask has been successful...


    public interface TaskCallbacks {
        void showToast(Boolean acrossWindows, int messageId);
    }

    private TaskCallbacks mCallbacks;
    private DummyTask mTask;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    /**
     * This method will only be called once
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
        // If we are returning here from a screen orientation
        // and the AsyncTask is still working, re-create and display the
        // progress dialog.
        if (isTaskRunning) {
            progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait a moment!");
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
     * Set the action to execute
     */
    public void setAction(String action){
        this.action = action;
    }


    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        // All dialogs should be closed before leaving the activity in order to avoid
        // the: Activity has leaked window com.android.internal.policy... exception
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * A dummy task that performs some (dumb) background work and
     * proxies progress updates and results back to the Activity.
     *
     * Note that we need to check if the callbacks are null in each
     * method in case they are invoked after the Activity's and
     * Fragment's onDestroy() method have been called.
     */
    private class DummyTask extends AsyncTask<String, Void, Pair<Boolean, Integer>> {


        // The four methods below are called by the TaskFragment when new
        // progress updates or results are available. The MainActivity
        // should respond by updating its UI to indicate the change.

        @Override
        public void onPreExecute() {
            isTaskRunning = true;
            Log.i("aquiw", "333");

            // enseñar el diálogo
            progressDialog = ProgressDialog.show(getActivity(),
                    getResources().getString(R.string.loadingTitle),
                    getResources().getString(R.string.loadingBody));
        }

        @Override
        public void onPostExecute(Pair<Boolean, Integer> result) {
            if (progressDialog != null) {
                // cerrar el diálogo
                progressDialog.dismiss();
            }
            isTaskRunning = false;
            if (result != null){
                // si hay mensaje enviarlo a la actividad padre
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
                Intent i = new Intent(getActivity(), NotesToSelf.class);
                startActivity(i);
            }
        }


        /**
         * Note that we do NOT call the callback object's methods
         * directly from the background thread, as this could result
         * in a race condition.
         */
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

                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(parametrosJSON.toString());
                    out.close();

                    JSONObject json = getJsonFromResponse(urlConnection);

                    // if ok
                    if (json.containsKey("success")){
                        // toast
                        // set the active username
                        Data.getMyData().setActiveUsername(json.get("success").toString());

                        // guardar el usuario activo en las preferencias
                        LogInActivity activity = (LogInActivity)getActivity();
                        if (activity != null){
                            activity.setActiveUsername(json.get("success").toString());
                        }

                        Log.i("aqui_active", Data.getMyData().getActiveUsername());
                        success = true;
                    }else {
                        String error = json.get("error").toString();
                        if (error.equals("wrong_credentials")){
                            // show toast
                            return Pair.create(true, R.string.incorrectPassword);
                        }else{
                            throw new Exception("connection_error");
                        }
                    }
                }else if(action == "fetchselfnotes"){
                    // se obtienen notas nuevas, es decir, si el usuario no tiene datos de
                    // aplicación se obtendrán todas, pero si hay realizado esta acción hace 5
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

                    // guardar fecha y hora del último fetch
                    SharedPreferences.Editor editor2= prefs_especiales.edit();
                    editor2.putString("lastFetchedDate", currentDate);
                    editor2.apply();

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

                    // si hay imagen ésta se descarga
                    JSONArray selfNotes = (JSONArray) json.get("success");
                    for (int i = 0; i < selfNotes.size(); i++) {
                        JSONObject row = (JSONObject) selfNotes.get(i);

                        Log.i("aqui_iteracion", row.toJSONString());

                        String imagePath = (String) row.get("imagePath");
                        String imageLocalPath = "";
                        String date = (String) row.get("date"); // FIXME no se si esto es así
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
                        MyDB gestorDB = new MyDB(getActivity(), "Notes", null, 1);
                        gestorDB.addSelfNote(activeUser, message, imageLocalPath, date);
                    }
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
                    Bitmap elBitmap = BitmapFactory.decodeStream(conn.getInputStream());

                    File eldirectorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String fileName = timeStamp + "_";

                    File imagenFich = new File(eldirectorio, fileName + ".jpg");
                    OutputStream os = new FileOutputStream(imagenFich);

                    int anchoImagen = elBitmap.getWidth();
                    int altoImagen = elBitmap.getHeight();

                    if (anchoImagen >= 240){
                        // redimensionar
                        altoImagen = ((altoImagen * 240) / anchoImagen);
                        elBitmap  = Bitmap.createScaledBitmap(elBitmap, 240, altoImagen, false);
                    }

                    elBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();

                    Log.i("aqui_downloadaimage", "end");
                    Log.i("aquiu_path", imagenFich.getPath());
                    return imagenFich.getPath();

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