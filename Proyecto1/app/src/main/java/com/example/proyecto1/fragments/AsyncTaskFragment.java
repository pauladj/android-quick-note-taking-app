package com.example.proyecto1.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.example.proyecto1.R;
import com.example.proyecto1.SignUpActivity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

            progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait a moment!");
        }

        @Override
        public void onPostExecute(Pair<Boolean, Integer> result) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            isTaskRunning = false;
            if (result != null){
                mCallbacks.showToast(result.first, result.second);
            }
            if (action.equals("signup") && success){
                // go to the login view
                SignUpActivity activity = (SignUpActivity)getActivity();
                if (activity != null){
                    activity.goToLogIn();
                }
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
                String direccion = "https://dawepauladj.webcindario.com/urls.php";
                HttpURLConnection urlConnection = null;

                URL destino = new URL(direccion);

                urlConnection = (HttpURLConnection) destino.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);

                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                // se forma el json y se procesa la respuesta según el método que se quiera

                if (action == "signup") {
                    JSONObject parametrosJSON = new JSONObject();
                    parametrosJSON.put("action", action);
                    parametrosJSON.put("username", strings[0]);
                    parametrosJSON.put("password", strings[1]);

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
                }
            } catch (Exception e) {
                // error
                // toast of error
                Log.i("aqui", e.toString());
                return Pair.create(true, R.string.serverError);
            }
            return null;
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

                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(result);
                    Log.i("aquijson", json.toString());
                    return json;
                }else{
                    throw new Exception("connection_error");
                }
            }catch (Exception e){
                // error
                // there was a connection error
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                throw new Exception("connection_error");
            }

        }

    }
}