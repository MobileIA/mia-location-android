package com.mobileia.location.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by matiascamiletti on 3/8/17.
 */

abstract public class LocationResultReceiver extends BroadcastReceiver {
    /**
     * Representa la Action que recibe las coordenadas
     */
    public static final String ACTION_LOCATION_RESULT_SUCCESS = "com.mobileia.location.ACTION_LOCATION_RESULT_SUCCESS";
    public static final String ACTION_LOCATION_RESULT_ERROR = "com.mobileia.location.ACTION_LOCATION_RESULT_ERROR";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Verificar si el Action coincide con este receiver
        if (null != intent && intent.getAction().equals(ACTION_LOCATION_RESULT_SUCCESS)) {
            // Obtener coordenadas enviadas
            double latitude = intent.getDoubleExtra("latitude", -1);
            double longitude = intent.getDoubleExtra("longitude", -1);
            // Llamar a metodo que obtiene las coordenadas
            onCoords(latitude, longitude);
        }else if(null != intent && intent.getAction().equals(ACTION_LOCATION_RESULT_ERROR)){
            onError();
        }
    }

    abstract public void onCoords(double latitude, double longitude);

    abstract public void onError();
}
