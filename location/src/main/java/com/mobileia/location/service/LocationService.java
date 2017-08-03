package com.mobileia.location.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mobileia.location.MobileiaLocation;
import com.mobileia.location.receiver.LocationResultReceiver;

/**
 * Created by matiascamiletti on 3/8/17.
 */

@SuppressWarnings("MissingPermission")
public class LocationService extends Service {
    /**
     * Servicio que se ejecuta al crear el servicio
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // Verificar si tiene permisos para pedir la localización
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // Ejecutar petición de localización
        requestLocation();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Metodo que realiza la petición
     */
    protected void requestLocation(){
        // Creamos servicio para pedir la localización
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        // Ejecutamos el pedido de localización
        client.requestLocationUpdates(LocationRequest.create(), createLocationCallback(), Looper.myLooper());
    }

    /**
     * Funcion que crea el Callback para la localizacion
     * @return
     */
    protected LocationCallback createLocationCallback(){
        return new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //Log.d(MobileiaLocation.TAG_DEBUG, "Location: " + locationResult.getLastLocation().getLatitude() + " - " + locationResult.getLastLocation().getLongitude());
                // Enviamos coordenadas
                sendLocationWithBroadcast(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
            }

        };
    }

    /**
     * Funcion que se encarga de enviar el broadcast
     * @param latitude
     * @param longitude
     */
    protected void sendLocationWithBroadcast(double latitude, double longitude) {
        // Creamos intent
        Intent locationIntent = new Intent();
        // Seteamos el Action correspondiente
        locationIntent.setAction(LocationResultReceiver.ACTION_LOCATION_RESULT_SUCCESS);
        // Asignamos coordenadas
        locationIntent.putExtra("latitude", latitude);
        locationIntent.putExtra("longitude", longitude);
        // Enviamos broadcast
        sendBroadcast(locationIntent);
    }
}
