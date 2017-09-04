package com.mobileia.location.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mobileia.authentication.MobileiaAuth;
import com.mobileia.location.MobileiaLocation;
import com.mobileia.location.receiver.LocationResultReceiver;

/**
 * Created by matiascamiletti on 3/8/17.
 */

@SuppressWarnings("MissingPermission")
public class LocationService extends Service {

    public static final int TYPE_CURRENT = 0;
    public static final int TYPE_BACKGROUND = 1;

    public static final String EXTRA_TYPE_SERVICE = "com.mobileia.location.EXTRA_TYPE_SERVICE";
    /**
     * Almacena el tipo de servicio que se inicio
     */
    protected int mType = 0;

    /**
     * Servicio que se ejecuta al crear el servicio
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // Verificar si tiene permisos para pedir la localización
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Enviamos mensaje de error
            sendErrorWithBroadcast("No tiene permisos");
            return;
        }
        // Ejecutar petición de localización
        requestLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Obtener parametro de Type
        mType = intent.getIntExtra(EXTRA_TYPE_SERVICE, 0);
        // Llamamos al padre
        return super.onStartCommand(intent, flags, startId);
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
        // Creamos LocationRequest
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setFastestInterval(5000);
        //request.setInterval(10000);
        //request.setExpirationTime(5000);
        //request.setExpirationDuration(5000);
        // Ejecutamos el pedido de localización
        client.requestLocationUpdates(request, createLocationCallback(), Looper.myLooper());
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
                Log.d(MobileiaLocation.TAG_DEBUG, "Location Result llego:");
                // Verificamos que tipo es
                if(mType == 0){
                    // Enviamos coordenadas
                    sendLocationWithBroadcast(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                }else if(mType == 1){
                    // Guardamos coordenadas del dispositivo en el servidor
                    saveLocationInServer(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                }
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

    /**
     * Funcion que se encarga de enviar el error al broadcast
     * @param error
     */
    protected void sendErrorWithBroadcast(String error){
        // Creamos intent
        Intent locationIntent = new Intent();
        // Seteamos el Action correspondiente
        locationIntent.setAction(LocationResultReceiver.ACTION_LOCATION_RESULT_ERROR);
        // Asignamos mensaje de error
        locationIntent.putExtra("message", error);
        // Enviamos broadcast
        sendBroadcast(locationIntent);
    }

    /**
     * Se encarga de guardar las coordenadas en el servidor
     * @param latitude
     * @param longitude
     */
    protected void saveLocationInServer(double latitude, double longitude){
        // Enviamos coordenadas al servidor
        MobileiaAuth.getInstance(getApplicationContext()).registerLocationThisDevice(latitude, longitude);
        // Cerramos servicio
        stopSelf();
    }
}
