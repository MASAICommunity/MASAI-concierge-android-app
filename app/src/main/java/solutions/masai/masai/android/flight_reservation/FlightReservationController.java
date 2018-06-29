package solutions.masai.masai.android.flight_reservation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.gson.Gson;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.general_base_controller.GeneralBaseController;
import solutions.masai.masai.android.core.model.journey.segment_types.AirSegment;

public class FlightReservationController extends GeneralBaseController implements FlightReservationView.FlightReservationViewListener {
    private FlightReservationView view;
    private Context context;
    private Handler handler;
    private MapView mapView;
    private GoogleMap map;
    private AirSegment airSegment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        handler = new Handler(Looper.getMainLooper());
        setContentView(R.layout.activity_flight_reservation);
        view = new FlightReservationView(findViewById(android.R.id.content), this, this);
        readFlight(getIntent());
        String departureName = "";
        String arrivalName = "";
        if (airSegment.getOriginCityName() != null) {
            departureName = airSegment.getOriginCityName();
        }
        if (airSegment.getDestinationCityName() != null) {
            arrivalName = airSegment.getDestinationCityName();
        }
        String sourceDestination = departureName.concat(" - ").concat(arrivalName);

        view.setTitle(sourceDestination);
        view.setInfo(airSegment);
    }

    public void readFlight(Intent intent) {
        String reservationJSON = intent.getStringExtra(C.EXTRA_FLIGHT);
        airSegment = new Gson().fromJson(reservationJSON, AirSegment.class);
    }

    @Override
    protected void onStop() {
        view.hideProgress();
        super.onStop();
    }

    @Override
    public void onNavigateBack() {
        onBackPressed();
    }

}
