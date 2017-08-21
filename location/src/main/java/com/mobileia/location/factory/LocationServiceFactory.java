package com.mobileia.location.factory;

import android.content.Context;
import android.content.Intent;

import com.mobileia.location.service.LocationService;

/**
 * Created by matiascamiletti on 3/8/17.
 */

public class LocationServiceFactory {

    public static void create(Context context, int type){
        // Creamos Intent para el servicio
        Intent intent = new Intent(context, LocationService.class);
        // Asignamos parametro de tipo
        intent.putExtra(LocationService.EXTRA_TYPE_SERVICE, type);
        // Iniciamos el servicio
        context.startService(intent);
    }

    public static void stop(Context context){
        // Creamos Intent para el servicio
        Intent intent = new Intent(context, LocationService.class);
        // Paramos el servicio
        context.stopService(intent);
    }
}
