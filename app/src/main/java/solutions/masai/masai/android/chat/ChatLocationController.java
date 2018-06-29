package solutions.masai.masai.android.chat;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import solutions.masai.masai.android.core.helper.DialogHelper;
import solutions.masai.masai.android.core.helper.GeolocationProvider;

/**
 * Created by Semko on 2017-05-25.
 */

public class ChatLocationController extends ChatFilePermissionController implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GeolocationProvider.GeolocationListener, DialogHelper.OnMapDialogCallback {
    private GoogleMap map;
    private LatLng latestPosition;
    private Marker marker;

    public void shareLocation(LatLng latestPosition) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GeolocationProvider.getInstance().init(this);
        GeolocationProvider.getInstance().setListener(this);
        map = googleMap;
        map.setOnMapClickListener(this);
        updatePosition();
    }

    @Override
    public void onShareLocation() {
        shareLocation(latestPosition);
    }

    private void updatePosition() {
        final Location location = GeolocationProvider.getInstance().getLastLocation();
        if (location != null) {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    moveToPosition(ll);
                }
            });
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        moveToPosition(latLng);
    }

    public void moveToPosition(LatLng position) {
        latestPosition = position;
        if (marker != null) {
            marker.remove();
        }
        marker = map.addMarker(new MarkerOptions().position(position));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(17).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onPositionChanged(Location location) {
        latestPosition = new LatLng(location.getLatitude(), location.getLongitude());
        updatePosition();
    }

    public void startMap() {
        DialogHelper.dialogMap(this, this, this);
    }
}
