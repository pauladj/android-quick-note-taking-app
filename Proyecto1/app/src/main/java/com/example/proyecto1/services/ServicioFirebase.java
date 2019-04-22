package com.example.proyecto1.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServicioFirebase extends FirebaseMessagingService {
    public ServicioFirebase() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("aqui", "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            // el mensaje viene con datos
            String messageType = remoteMessage.getData().get("type");
            String content = remoteMessage.getData().get("content");
            String timestamp = remoteMessage.getData().get("timestamp");

            // aqui
            //https://stackoverflow.com/questions/43140472/android-firebase-messaging-how-update-ui-from-onmessagereceived
            // https://stackoverflow.com/questions/8802157/how-to-use-localbroadcastmanager

            // avisar a la actividad selfnote de una nueva nota
            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());
            Intent intent = new Intent("receivedmessage");
            intent.putExtra("type", messageType);
            intent.putExtra("content", content);
            intent.putExtra("timestamp", timestamp);
            broadcaster.sendBroadcast(intent);
        }
        if (remoteMessage.getNotification() != null) {
            // el mensaje es una notificación

        }
    }
}
