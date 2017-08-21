package com.mobileia.location;

import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.util.Log;

import com.mobileia.location.factory.LocationReceiverFactory;
import com.mobileia.location.factory.LocationServiceFactory;
import com.mobileia.location.receiver.LocationResultReceiver;
import com.mobileia.location.service.LocationService;

/**
 * Created by matiascamiletti on 3/8/17.
 */

public class MobileiaLocation {
    /**
     * Constante para debug
     */
    public static final String TAG_DEBUG = "MobileiaLocation";
    /**
     * Almacena el contexto
     */
    protected Context mContext;

    /**
     * Constructor que requiere el contexto
     * @param context
     */
    public MobileiaLocation(Context context){ this.mContext = context; }

    /**
     * Funcion que se encarga de realizar una petición de localización
     * @param callback
     */
    public void requestLocation(final OnLocationResult callback){
        // Registramos el receiver que va a obtener las coordenadas
        registerReceiver(new LocationResultReceiver(){
            @Override
            public void onCoords(double latitude, double longitude) {
                // Enviamos coordenadas
                callback.onLocation(latitude, longitude);
                // Eliminamos receiver
                mContext.unregisterReceiver(this);
                // Paramos el servicio
                LocationServiceFactory.stop(mContext);
            }

            @Override
            public void onError() {
                // Ejecutamos el callback
                callback.onError();
                // Eliminamos receiver
                mContext.unregisterReceiver(this);
                // Paramos el servicio
                LocationServiceFactory.stop(mContext);
            }
        });
        // Ejecutamos el servicio
        LocationServiceFactory.create(mContext, LocationService.TYPE_CURRENT);
    }

    /**
     * Funcion que se encarga de iniciar el servicio para que guarde la localización en el servidor
     */
    public void requestLocationForSendToServer(){
        // Ejecutamos el servicio
        LocationServiceFactory.create(mContext, LocationService.TYPE_BACKGROUND);
    }

    /**
     * Funcion para iniciar el servicio en background que obtiene la localización.
     */
    public void startBackgroundService(){
        // Creamos LocationReceiver
        LocationReceiverFactory.create(mContext);
    }

    /**
     * Funcion que se encarga de calcular la distanta entre dos coordenadas en metros
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public float[] distanceBetween(double lat1, double lng1, double lat2, double lng2){
        float[] results = new float[1];
        Location.distanceBetween(lat1, lng1, lat2, lng2, results);
        return results;
    }

    /**
     * Funcion que registra el receiver de localización
     * @param receiver
     */
    protected void registerReceiver(LocationResultReceiver receiver){
        // Creamos intent con el action correspondiente
        IntentFilter intentFilter = new IntentFilter(LocationResultReceiver.ACTION_LOCATION_RESULT_SUCCESS);
        intentFilter.addAction(LocationResultReceiver.ACTION_LOCATION_RESULT_ERROR);
        // Registramos el receiver
        mContext.registerReceiver(receiver, intentFilter);
    }

    /**
     * Interface para recibir respuesta de localización
     */
    public interface OnLocationResult{
        void onLocation(double latitude, double longitude);
        void onError();
    }
}
