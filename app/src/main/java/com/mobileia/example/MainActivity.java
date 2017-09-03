package com.mobileia.example;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mobileia.authentication.rest.RestGenerator;
import com.mobileia.core.Mobileia;
import com.mobileia.location.MobileiaLocation;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mobileia.getInstance().setAppId(8);
        // Iniciamos receiver
        //LocationReceiverFactory.create(this);

        // Pedimos permisos de localización
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                })
                .check();

        // Iniciamos servicio background
        new MobileiaLocation(this).startBackgroundService();
    }

    public void onClick(View v){
        new MobileiaLocation(this).requestLocation(new MobileiaLocation.OnLocationResult() {
            @Override
            public void onLocation(double latitude, double longitude) {
                Log.d(MobileiaLocation.TAG_DEBUG, "Location MainActivity: " + latitude + " - " + longitude);
            }

            @Override
            public void onError() {
                Log.d(MobileiaLocation.TAG_DEBUG, "Location MainActivity:  ERROR ");
            }
        });
    }
}
