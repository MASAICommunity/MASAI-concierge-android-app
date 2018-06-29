package solutions.masai.masai.android.train_reservation;

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
import solutions.masai.masai.android.core.model.journey.segment_types.RailSegment;

public class TrainReservationController extends GeneralBaseController implements TrainReservationView.PublicTransportationViewListener {
    private TrainReservationView view;
    private Context context;
    private Handler handler;
    private MapView mapView;
    private GoogleMap map;
    private RailSegment publicTransportation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        handler = new Handler(Looper.getMainLooper());
        setContentView(R.layout.activity_train_reservation);
        readPublicTransportation(getIntent());
        view = new TrainReservationView(findViewById(android.R.id.content), this, this);
/*        String departureName = publicTransportation.getOriginName();
        String arrivalName = publicTransportation.getDestinationCityName();
        if (departureName == null) {
            departureName = "";
        }
        if (arrivalName == null) {
            arrivalName = "";
        }
        String sourceDestination = departureName.concat(" - ").concat(arrivalName);*/
        view.setTitle(publicTransportation.getRailLine().concat(" ").concat(publicTransportation.getTrainNumber()));
        view.setInfo(publicTransportation);
    }

    public void readPublicTransportation(Intent intent) {
        String reservationJSON = intent.getStringExtra(C.EXTRA_TRAIN);
        publicTransportation = new Gson().fromJson(reservationJSON, RailSegment.class);
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
