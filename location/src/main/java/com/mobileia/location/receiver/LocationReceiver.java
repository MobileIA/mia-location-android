package com.mobileia.location.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mobileia.authentication.MobileiaAuth;
import com.mobileia.location.MobileiaLocation;

/**
 * Created by matiascamiletti on 3/8/17.
 */

public class LocationReceiver extends BroadcastReceiver {
    /**
     * Code para identificar el receiver
     */
    public static final int REQUEST_CODE = 8565;

    @Override
    public void onReceive(final Context context, Intent intent) {
        // Verificar si el usuario esta logueado
        if(MobileiaAuth.getInstance(context).getCurrentUser() == null){
            return;
        }
        // Pedir Localización
        new MobileiaLocation(context).requestLocationForSendToServer();
    }
}
