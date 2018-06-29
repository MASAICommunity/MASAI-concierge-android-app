package solutions.masai.masai.android.car_reservation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.general_base_controller.GeneralBaseController;
import solutions.masai.masai.android.core.model.journey.segment_types.CarSegment;

public class CarReservationController extends GeneralBaseController implements CarReservationView.ActivityViewListener {
    private CarReservationView view;
    private Context context;
    private Handler handler;
    private CarSegment carSegment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        handler = new Handler(Looper.getMainLooper());
        setContentView(R.layout.activity_car_reservation);
        readPublicTransportation(getIntent());
        view = new CarReservationView(findViewById(android.R.id.content), this, this);

        view.setTitle(context.getString(R.string.car));
        view.setInfo(carSegment);
    }

    public void readPublicTransportation(Intent intent) {
        String reservationJSON = intent.getStringExtra(C.EXTRA_CAR);
        carSegment = new Gson().fromJson(reservationJSON, CarSegment.class);
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
