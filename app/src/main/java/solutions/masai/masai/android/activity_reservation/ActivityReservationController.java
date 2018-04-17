package solutions.masai.masai.android.activity_reservation;

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
import solutions.masai.masai.android.core.model.journey.segment_types.ActivitySegment;

public class ActivityReservationController extends GeneralBaseController implements ActivityReservationView.ActivityViewListener {
    private ActivityReservationView view;
    private Context context;
    private Handler handler;
    private MapView mapView;
    private GoogleMap map;
    private ActivitySegment activitySegment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        handler = new Handler(Looper.getMainLooper());
        setContentView(R.layout.activity_activity_reservation);
        readPublicTransportation(getIntent());
        view = new ActivityReservationView(findViewById(android.R.id.content), this, this);

        view.setTitle(context.getString(R.string.activity));
        view.setInfo(activitySegment);
    }

    public void readPublicTransportation(Intent intent) {
        String reservationJSON = intent.getStringExtra(C.EXTRA_ACTIVITY);
        activitySegment = new Gson().fromJson(reservationJSON, ActivitySegment.class);
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
