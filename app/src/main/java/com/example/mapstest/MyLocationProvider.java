package com.example.mapstest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

@SuppressLint("MissingPermission")
public class MyLocationProvider {

    Context context;
    LocationManager locationManager;
    Location location;


    private final static long TIME_LOCATION_UPDATE = 5 * 1000;
    private final static float DISTANCE_LOCATION_UPDATE = 400;


    public MyLocationProvider(Context context) {
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.location = null;
    }

    public boolean providerEnabled() {
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }


    public Location getCurrentLocation(LocationListener locationListener) {

        if (!providerEnabled())
            return null;

        String provider = LocationManager.GPS_PROVIDER;
        if (!locationManager.isProviderEnabled(provider))
            provider = LocationManager.NETWORK_PROVIDER;



        location = locationManager.getLastKnownLocation(provider);
        if (location == null)
            location = getBestLocation();

        if (locationListener != null) {
            locationManager.
                    requestLocationUpdates(provider, TIME_LOCATION_UPDATE
                            , DISTANCE_LOCATION_UPDATE, locationListener);
        }


        return location;

    }

    private Location getBestLocation() {
        List<String> providers = locationManager.getAllProviders();

        Location bestLocation = null;

        for (String provider : providers) {



            Location temp = locationManager.getLastKnownLocation(provider);

            if (temp == null)
                continue;

            if (bestLocation == null)
                bestLocation = temp;

            else
            {
             if (temp.getAccuracy()>bestLocation.getAccuracy())
                 bestLocation=temp;
            }
        }

        return bestLocation;
    }
}
