package com.example.proyecto1.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
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

        }
        if (remoteMessage.getNotification() != null) {
            // el mensaje es una notificación

        }
    }
}
