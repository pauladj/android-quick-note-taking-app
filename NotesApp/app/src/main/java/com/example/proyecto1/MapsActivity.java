package com.example.proyecto1;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.example.proyecto1.utilities.Data;
import com.example.proyecto1.utilities.MyDB;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment elfragmento =
                (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentoMapa);
        elfragmento.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // obtener latitud y longitud de las notas
        MyDB gestorDB = new MyDB(this, "Notes", null, 1);
        ArrayList<ArrayList<String>> notes =
                gestorDB.getMapPositionsByUser(Data.getMyData().getActiveUsername());
        if (notes == null){
            // error de la base de datos
            int tiempo = Toast.LENGTH_SHORT;
            Context context = this;
            Toast aviso = Toast.makeText(context, getResources().getString(R.string.databaseError), tiempo);
            aviso.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 100);
            aviso.show();
            return;
        }
        ArrayList<String> notesTitles = notes.get(0);
        ArrayList<String> notesLatitud = notes.get(1);
        ArrayList<String> notesLongitud = notes.get(2);

        for (int i=0; i<notesTitles.size(); i++){
            double latitud = Double.parseDouble(notesLatitud.get(i));
            double longitud = Double.parseDouble(notesLongitud.get(i));

            // añadir marcador para cada nota
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitud, longitud))
                    .title(notesTitles.get(i)));
        }

    }
}
