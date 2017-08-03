package com.mobileia.location.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
    public void onReceive(Context context, Intent intent) {
        Log.d(MobileiaLocation.TAG_DEBUG, "LocationReceiver Receive.");
    }
}
