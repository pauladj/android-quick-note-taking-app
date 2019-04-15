package com.example.proyecto1;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.example.proyecto1.fragments.AsyncTaskFragment;

public class Common extends AppCompatActivity  implements AsyncTaskFragment.TaskCallbacks  {

    /**
     * Show a toast in the view
     * @param acrossWindows - if true the toast does not disappear when view changes
     * @param messageId - the message id to show
     */
    public void showToast(Boolean acrossWindows, int messageId){
        int tiempo = Toast.LENGTH_SHORT;
        Context context;
        if (acrossWindows){
            context = getApplicationContext();
        }else{
            context = this;
        }
        Toast aviso = Toast.makeText(context, getResources().getString(messageId), tiempo);
        aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
        aviso.show();
    }

    /**
     * Get the active username
     * @param - the active username (token)
     */
    public String getActiveUsername(){
        SharedPreferences prefs_especiales= getSharedPreferences(
                "preferencias_especiales",
                Context.MODE_PRIVATE);

        return prefs_especiales.getString("activeUsername", null);
    }

    /**
     * Set the active username
     * @param username - the active username
     */
    public void setActiveUsername(String username){
        SharedPreferences prefs_especiales= getSharedPreferences(
                "preferencias_especiales",
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor2= prefs_especiales.edit();
        editor2.putString("activeUsername", username);
        editor2.apply();
    }
}
