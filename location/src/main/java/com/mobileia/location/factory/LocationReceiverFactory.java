package com.mobileia.location.factory;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.mobileia.location.receiver.LocationReceiver;

/**
 * Created by matiascamiletti on 3/8/17.
 */

public class LocationReceiverFactory {
    /**
     * Funcion creadora para el LocationReceiver
     * @param context
     */
    public static void create(Context context){
        // Creamos el intent con el Receiver correspondiente
        Intent intent = new Intent(context.getApplicationContext(), LocationReceiver.class);
        // Creamos el PendingIntent que se ejecutara cada cierto tiempo
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, LocationReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Obtenemos el servicio de Alarmas
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Seteamos la alarma recursiva para ejecutar el receiver
        //alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pIntent);
    }

    /**
     * Funcion de backup por si es necesario pausar la alarma recurrente
     * @param context
     */
    public static void stop(Context context){
        // Creamos el intent con el Receiver correspondiente
        Intent intent = new Intent(context.getApplicationContext(), LocationReceiver.class);
        // Creamos el PendingIntent identico para cancelar
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, LocationReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Obtenemos el servicio de Alarmas
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Cancelamos la alarma recurrente
        alarm.cancel(pIntent);
    }
}
