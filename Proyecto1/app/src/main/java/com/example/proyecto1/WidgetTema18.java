package com.example.proyecto1;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.proyecto1.utilities.MyDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetTema18 extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_tema18);

        String valor = "?";

        // cuantas notas tengo en total
        SharedPreferences prefs_especiales = context.getSharedPreferences(
                "preferencias_especiales",
                Context.MODE_PRIVATE);
        String activeUser = prefs_especiales.getString("activeUsername", null);

        if (activeUser != null){
            // hay un usuario que ha iniciado sesión
            MyDB gestorDB = new MyDB(context, "Notes", null, 1);
            ArrayList<ArrayList<String>> data = gestorDB.getNotesDataByUser(activeUser);
            if (data != null){
                // no ha habido errores obteniendo datos de la base de datos
                valor = String.valueOf(data.get(0).size());
                if (data.get(0).size() > 10000){
                    valor = "10000+";
                }
            }
        }

        // aqui https://stackoverflow.com/a/26568544/11002531
        Intent intentSync = new Intent(context, WidgetTema18.class);
        intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intentSync.putExtra( AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId } );
        PendingIntent pendingSync = PendingIntent.getBroadcast(context,0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.loadButton,pendingSync);

        views.setTextViewText(R.id.appwidget_text, valor);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

