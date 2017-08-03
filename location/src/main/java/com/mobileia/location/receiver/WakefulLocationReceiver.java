package com.mobileia.location.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.mobileia.location.MobileiaLocation;
import com.mobileia.location.factory.LocationReceiverFactory;

/**
 * Created by matiascamiletti on 3/8/17.
 */

public class WakefulLocationReceiver extends WakefulBroadcastReceiver {
    /**
     * Metodo que se ejecuta al encender el dispositivo
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MobileiaLocation.TAG_DEBUG, "Inicio servicio despertador!");
        // Creamos LocationReceiver
        LocationReceiverFactory.create(context);
    }
}
