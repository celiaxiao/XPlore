package com.example.navucsd;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;

/**
 * Usage: Call the following function in your activity to get the latest updated location:
 *        Location location = GpsUtil.getInstance(yourActivityName.this).getLastLocation();
 *
 *        Then, use the following functions to get latitude or longitude:
 *        double latitude = location.getLatitude();
 *        double longitude = location.getLongitude();
 */
public class GpsUtil {

    private static GpsUtil instance;
    private LocationManager locationManager;
    private Context mContext;
    private String locationProvider;
    private Location location;

    private GpsUtil(Context context) {
        this.mContext = context;
        getLocation();
    }

    public static GpsUtil getInstance(Context context) {
        if (instance == null) {
            instance = new GpsUtil(context);
        }
        return instance;
    }

    private void getLocation() {

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            Log.d("location", "GPS location provider");
            locationProvider = LocationManager.GPS_PROVIDER;
        }
        else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            Log.d("location", "NETWORK location provider");
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }
        else {
            Log.d("location", "no location provider");
            // Consider pop up an alert
            return;
        }

        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Update location every 1 min
        locationManager.requestLocationUpdates(locationProvider, 60000, 0, locationListener);

        location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            setLocation(location);
        }
        else {
            Log.d("location","location = null");
        }

    }

    private void setLocation(Location location) {
        this.location = location;
        Log.d( "location", "latitude:" + location.getLatitude() );
        Log.d( "location", "longtitude:" + location.getLongitude() );
    }

    public Location getLastLocation() { return location; }

    public void removeLocationUpdatesListener() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager != null) {
            instance = null;
            locationManager.removeUpdates( locationListener );
        }
    }

    /**
     * Location Listener
     */
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}
        @Override
        public void onProviderEnabled(String s) {}
        @Override
        public void onProviderDisabled(String s) {}
        @Override
        public void onLocationChanged(Location location) {
            setLocation(location);
        }
    };}
