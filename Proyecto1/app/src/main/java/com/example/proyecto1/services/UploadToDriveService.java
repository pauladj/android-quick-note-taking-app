package com.example.proyecto1.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.proyecto1.R;
import com.example.proyecto1.SingleNoteActivity;
import com.example.proyecto1.utilities.DriveServiceHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.util.Collections;

public class UploadToDriveService extends Service {

    private NotificationCompat.Builder elBuilder;
    private NotificationManager elManager;
    private int progress;
    private int idNotification;


    /**
     * Update the notification progress
     */
    private void updateNotification(){
        elBuilder.setContentText(""+progress+"%");
        elBuilder.setProgress(100, progress,false);
        elManager.notify(idNotification, elBuilder.build()); // start notification
        if (progress == 100){
            uploadCompleteNotification();
        }
    }

    /**
     * If the upload is complete
     */
    private void uploadCompleteNotification(){
        elBuilder.setContentText("Upload Complete");
        elBuilder.setOngoing(false);
        elManager.notify(idNotification, elBuilder.build());

        stopSelf();
    }

    /**
     * If the upload failed
     */
    private void uploadFailedNotification(){
        elBuilder.setContentText("Upload failed");
        elBuilder.setOngoing(false);
        elManager.notify(idNotification, elBuilder.build());

        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // se obtienen los datos asociados al intent
        String fileName = intent.getStringExtra("fileName");
        String fileContent = intent.getStringExtra("fileContent");

        // obtener id de la notificación
        SharedPreferences prefs_especiales= getSharedPreferences("preferencias_especiales",
                Context.MODE_PRIVATE);

        // the initial value is 3 if it's the first time, but we have to add one to this
        idNotification = prefs_especiales.getInt("id", 2) + 1;

        SharedPreferences.Editor editor2= prefs_especiales.edit();
        // the initial value is 3
        editor2.putInt("id",idNotification);
        editor2.apply();


        // notificación
        elManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        elBuilder = new NotificationCompat.Builder(this,
                "uploadNote");

        elBuilder.setSmallIcon(R.drawable.ic_upload)
                .setContentTitle("titulo")
                .setContentText("textp")
                .setAutoCancel(false)
                .setOngoing(true) // el usuario no puede quitar la notificación
                .setGroup("uploadNoteGroup") // notificaciones anidadas
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setProgress(100,0,false);

        if (Build.VERSION.SDK_INT >= 21) {
            elBuilder.setVibrate(new long[0]);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("uploadNote",
                    "uploadNote",
                    NotificationManager.IMPORTANCE_HIGH);
            elCanal.setDescription("uploadNote");
            elCanal.enableLights(true);
            elCanal.setGroup("uploadNoteGroup"); // notificaciones anidadas
            elCanal.setLightColor(Color.BLUE);
            elCanal.setVibrationPattern(new long[0]);
            elCanal.enableVibration(true);

            elManager.createNotificationChannel(elCanal);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // notificación anidada principal
            Notification summaryNotification =
                    new NotificationCompat.Builder(this, "uploadNote")
                            .setContentTitle("titulo principal")
                            //set content text to support devices running API level < 24
                            .setContentText("texto principal")
                            .setSmallIcon(R.drawable.ic_upload)
                            //specify which group this notification belongs to
                            .setGroup("uploadNoteGroup")
                            //set this notification as the summary for the group
                            .setGroupSummary(true)
                            .setAutoCancel(false)
                            .setOngoing(true) // el usuario no puede quitar la notificación
                            .build();
            elManager.notify(idNotification, summaryNotification);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(idNotification, elBuilder.build());
        }else{
            elManager.notify(idNotification, elBuilder.build()); // start notification
        }


        // el servicio se ejecuta
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(
                        getApplicationContext(), Collections.singleton(DriveScopes.DRIVE));
        credential.setSelectedAccount(googleAccount.getAccount());
        Drive googleDriveService =
                new Drive.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new GsonFactory(),
                        credential)
                        .setApplicationName("Drive API")
                        .build();

        DriveServiceHelper.getMiDriveServiceHelper(googleDriveService);

        // comprobar si la carpeta de la aplicación notes existe
        DriveServiceHelper.getMiDriveServiceHelper().folderExists("notes_android_app")
                .addOnSuccessListener(existingFolderId -> {
                    progress = 25;
                    updateNotification();
                    if (existingFolderId == null){
                        Log.i("aqui", "2");
                        //Folder does not exist, create it
                        DriveServiceHelper.getMiDriveServiceHelper().createFile("notes_android_app",
                                "application/vnd.google-apps.folder", "root")
                            .addOnSuccessListener(newFolderId -> {
                                // Folder created
                                progress += 25;
                                updateNotification();
                                Log.i("aqui", "5");

                                // primero crear el fichero
                                DriveServiceHelper.getMiDriveServiceHelper().createFile(fileName,
                                        "text/html", newFolderId)
                                        .addOnSuccessListener(fileId -> {
                                            Log.i("aqui", "6");
                                            progress += 25;
                                            updateNotification();
                                            // y luego actualizar sus datos
                                            DriveServiceHelper.getMiDriveServiceHelper().saveFile(fileId, fileName, "text/html", fileContent)
                                                    .addOnSuccessListener((k) -> {
                                                        Log.i("aqui", "7");
                                                        progress += 25;
                                                        updateNotification();
                                                    })
                                                    .addOnFailureListener(exception -> {
                                                        Log.i("aqui", "8");

                                                        uploadFailedNotification();
                                                    });
                                        })
                                        .addOnFailureListener(exception -> {
                                            Log.i("aqui", "9");

                                            uploadFailedNotification();
                                        });
                            })
                            .addOnFailureListener(exception -> {
                                Log.i("aqui", "10");

                                uploadFailedNotification();
                            });
                    }else{
                        // Folder exists
                        Log.i("aqui", "11");

                        // upload the note
                        Log.i("aqui", "13");

                        // primero crear el fichero
                        DriveServiceHelper.getMiDriveServiceHelper().createFile(fileName,
                                "text/html", existingFolderId)
                                .addOnSuccessListener(fileId -> {
                                    Log.i("aqui", "6");
                                    progress += 35;
                                    updateNotification();
                                    // y luego actualizar sus datos
                                    DriveServiceHelper.getMiDriveServiceHelper().saveFile(fileId, fileName, "text/html", fileContent)
                                            .addOnSuccessListener((k) -> {
                                                progress += 40;
                                                updateNotification();
                                                Log.i("aqui", "7");

                                            })
                                            .addOnFailureListener(exception -> {
                                                Log.i("aqui", "8");

                                                uploadFailedNotification();
                                            });
                                })
                                .addOnFailureListener(exception -> {
                                    Log.i("aqui", "9");

                                    uploadFailedNotification();
                                });

                    }
                })
                .addOnFailureListener(exception -> {
                    uploadFailedNotification();
                    Log.i("aqui", "17");
                    Log.i("aqui17", exception.toString());
                });

        return START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
