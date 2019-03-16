package com.example.proyecto1.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.proyecto1.MainActivity;
import com.example.proyecto1.R;
import com.example.proyecto1.dialogs.ConfimExit;

public class PreferencesFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    boolean changedNotesOrder = false;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.conf_preferencias);

        if (bundle!=null){
            if (bundle.containsKey("changedNotesOrder")){
                changedNotesOrder = bundle.getBoolean("changedNotesOrder");
            }
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences prefs, String key){
        if (key.equals("orden")){
           changedNotesOrder = true;
       }else if(key.equals("notifications")){
            boolean notificationsActive = prefs.getBoolean("notifications", false);
            if (notificationsActive){
                // notify the user with a notification
                NotificationManager elManager =
                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(getActivity(),
                        "preferences");
                // configure it
                elBuilder.setSmallIcon(R.drawable.ic_settings)
                        .setContentTitle(getResources().getString(R.string.notifications_preferencesNotifications_title))
                        .setContentText(getResources().getString(R.string.notifications_preferencesNotifications_text))
                        .setVibrate(new long[] {0, 500})
                        .setAutoCancel(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel elCanal = new NotificationChannel("preferences",
                            "preferences",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    elCanal.setDescription("preferences");
                    elCanal.enableLights(true);
                    elCanal.setLightColor(Color.BLUE);
                    elCanal.setVibrationPattern(new long[]{0, 500});
                    elCanal.enableVibration(true);
                    elManager.createNotificationChannel(elCanal);
                }
                elManager.notify(1, elBuilder.build()); // start notification
            }
       }
       // Notification saying that the preference has been changed
        int tiempo = Toast.LENGTH_SHORT;
        Toast aviso = Toast.makeText(getActivity(), R.string.preferences_saved, tiempo);
        aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
        aviso.show();
    }

    /**
     * Check if the order has changed
     * @return - true if the order of the notes has changed, false if it hasn't changed
     */
    public boolean noteOrderChanged(){
        return changedNotesOrder;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("changedNoteOrder", changedNotesOrder);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
