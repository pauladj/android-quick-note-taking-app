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


    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    public interface TaskCallbacks {

    }

    private TaskCallbacks mCallbacks;
    private DummyTask mTask;

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
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
    private class DummyTask extends AsyncTask<String, Void, String> {


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
        public void onPostExecute(String result) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            isTaskRunning = false;
        }

        /**
         * Note that we do NOT call the callback object's methods
         * directly from the background thread, as this could result
         * in a race condition.
         */
        @Override
        protected String doInBackground(String... strings) {
            Log.i("aqui", "backgro");
            String action = strings[0];
            Log.i("aqui", action);
            if (action == "signup") {
                String direccion = "https://dawepauladj.webcindario.com/urls.php";
                HttpURLConnection urlConnection = null;
                try {
                    URL destino = new URL(direccion);

                    urlConnection = (HttpURLConnection) destino.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);

                    JSONObject parametrosJSON = new JSONObject();
                    parametrosJSON.put("action", action);
                    parametrosJSON.put("username", strings[1]);
                    parametrosJSON.put("password", strings[2]);

                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/json");

                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(parametrosJSON.toString());
                    out.close();

                    int statusCode = urlConnection.getResponseCode();
                    Log.i("aqui", urlConnection.getResponseMessage());
                    Log.i("aquistatus", String.valueOf(statusCode));

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
                        Log.i("aqui", json.toString());
                    }
                } catch (Exception e) {
                    // error
                    Log.i("aqui", e.toString());
                }
            }
            return "d";
        }



    }
}