package com.mobileia.location;

import android.content.Context;
import android.content.IntentFilter;

import com.mobileia.location.factory.LocationServiceFactory;
import com.mobileia.location.receiver.LocationResultReceiver;

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
        });
        // Ejecutamos el servicio
        LocationServiceFactory.create(mContext);
    }

    protected void registerReceiver(LocationResultReceiver receiver){
        // Creamos intent con el action correspondiente
        IntentFilter intentFilter = new IntentFilter(LocationResultReceiver.ACTION_LOCATION_RESULT_SUCCESS);
        //intentFilter.addAction(SettingsLocationTracker.ACTION_PERMISSION_DEINED);
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
