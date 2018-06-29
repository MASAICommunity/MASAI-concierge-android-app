package solutions.masai.masai.android.core.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GeolocationProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final long TEN_MINUTES = 600000;
    private static final long SECONDS_10 = 10000;
    private static GeolocationProvider instance;
    private static Location currentLocation = null;
    private static long lastUpdate;
    private boolean requestingLocationUpdates = false;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Context context;
    private GeolocationListener listener;
    private Handler handler;

    private GeolocationProvider() {
        handler = new Handler(Looper.getMainLooper());
    }

    public static GeolocationProvider getInstance() {
        if (instance == null) {
            instance = new GeolocationProvider();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        if (googleApiClient == null || locationRequest == null) {
            requestingLocationUpdates = false;
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            locationRequest = new LocationRequest();
            locationRequest.setInterval(TEN_MINUTES);
            locationRequest.setFastestInterval(SECONDS_10);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    public void setListener(GeolocationListener listener) {
        handler.removeCallbacksAndMessages(null);
        if (!googleApiClient.isConnected() && !googleApiClient.isConnecting()) {
            googleApiClient.connect();
        }
        this.listener = listener;
    }

    public void removeListener() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stop();
            }
        }, 3000);
        this.listener = null;
    }

    private void startLocationUpdates() {
        if (googleApiClient != null && hasGeolocationPermission() && googleApiClient.isConnected() && !requestingLocationUpdates) {
            requestingLocationUpdates = true;
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, instance);
        }
    }

    public void stop() {
        requestingLocationUpdates = false;
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, instance);
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public Location getLastLocation() {
        if (currentLocation != null) {
            return currentLocation;
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        if (listener != null) {
            listener.onPositionChanged(location);
        }
        lastUpdate = System.currentTimeMillis();
    }

    private boolean hasGeolocationPermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public interface GeolocationListener {
        void onPositionChanged(Location location);
    }
}