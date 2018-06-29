package solutions.masai.masai.android.hotel_reservation;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.general_base_controller.GeneralBaseController;
import solutions.masai.masai.android.core.model.journey.segment_types.HotelSegment;

import java.util.Locale;

public class HotelReservationController extends GeneralBaseController implements HotelReservationView.HotelReservationViewListener, OnMapReadyCallback {
    private HotelReservationView view;
    private Context context;
    private Handler handler;
    private MapView mapView;
    private GoogleMap map;
    private HotelSegment hotelReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        handler = new Handler(Looper.getMainLooper());
        setContentView(R.layout.activity_hotel_reservation);
        readHotelReservation(getIntent());
        view = new HotelReservationView(findViewById(android.R.id.content), this, this);
        view.setTitle(hotelReservation.getHotelName());
        view.setInfo(hotelReservation);
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    public void readHotelReservation(Intent intent) {
        String reservationJSON = intent.getStringExtra(C.EXTRA_HOTEL_RESERVATION);
        hotelReservation = new Gson().fromJson(reservationJSON, HotelSegment.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        view.hideProgress();
        super.onStop();
    }

    private void startMap(float latitude, float longitude, String name) {
        try {
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", latitude, longitude, name);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.app_not_found, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapClicked() {
        startMap(Float.parseFloat(hotelReservation.getLat()), Float.parseFloat(hotelReservation.getLon()), hotelReservation.getHotelName());
    }

    @Override
    public void onPhoneClicked() {
       /* try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:".concat(hotelReservation.getPhone())));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.app_not_found, Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public void onNavigateBack() {
        onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng ll = new LatLng(Double.parseDouble(hotelReservation.getLat()), Double.parseDouble(hotelReservation.getLon()));
        map.addMarker(new MarkerOptions().position(ll));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(ll)
                .zoom(17).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
