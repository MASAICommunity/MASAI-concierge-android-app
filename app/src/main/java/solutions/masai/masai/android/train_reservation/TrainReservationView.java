package solutions.masai.masai.android.train_reservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import solutions.masai.masai.android.R;

import solutions.masai.masai.android.core.helper.general_base_controller.GeneralBaseView;
import solutions.masai.masai.android.core.model.journey.segment_types.RailSegment;

import solutions.masai.masai.android.core.helper.DateTimeHelper;

/**
 * Created by Semko on 2016-12-05.
 */

public class TrainReservationView extends GeneralBaseView {
    private View rootView;
    private PublicTransportationViewListener listener;
    private Context context;
    private Handler handler;
    private TextView tvTitle;
    private TextView tvTrainNumber;
    private TextView tvDepartureStationName;
    private TextView tvArrivalStationName;
    private TextView tvDepartureDate;
    private TextView tvDepartureTime;
    private TextView tvArrivalDate;
    private TextView tvArrivalTime;
    private TextView tvArrivalGate;
    private TextView tvDepartureGate;
    private TextView tvPassenger;
    private TextView tvTravelTicket;
    private TextView tvConfirmationNumber;
    private TextView tvSeat;
    private TextView tvClassType;
    private TextView tvLink;
    private ImageView ivBack;
    private ProgressDialog progressDialog;

    TrainReservationView(View rootView, Context context, PublicTransportationViewListener listener) {
        super(rootView, context);
        this.rootView = rootView;
        this.context = context;
        this.listener = listener;
        handler = new Handler(Looper.getMainLooper());
        initViews();
    }

    private void initViews() {
        progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(rootView.getContext().getString(R.string.loading_data));
        progressDialog.setCanceledOnTouchOutside(false);
        tvTrainNumber = (TextView) rootView.findViewById(R.id.activity_public_transportation_train_number);
        tvDepartureStationName = (TextView) rootView.findViewById(R.id.activity_public_transportation_from);
        tvArrivalStationName = (TextView) rootView.findViewById(R.id.activity_public_transportation_to);
        tvDepartureDate = (TextView) rootView.findViewById(R.id.activity_public_transportation_date);
        tvDepartureTime = (TextView) rootView.findViewById(R.id.activity_public_transportation_time);
        tvArrivalDate = (TextView) rootView.findViewById(R.id.activity_public_transportation_arrival_date);
        tvArrivalTime = (TextView) rootView.findViewById(R.id.activity_public_transportation_arrival_time);
        tvArrivalGate = (TextView) rootView.findViewById(R.id.activity_public_transportation_arrival_gate);
        tvDepartureGate = (TextView) rootView.findViewById(R.id.activity_public_transportation_departure_gate);
        tvPassenger = (TextView) rootView.findViewById(R.id.activity_public_transportation_passenger_name);
        tvTravelTicket = (TextView) rootView.findViewById(R.id.activity_public_transportation_ticket);
        tvConfirmationNumber = (TextView) rootView.findViewById(R.id.activity_public_transportation_confirmation_number);
        tvSeat = (TextView) rootView.findViewById(R.id.activity_public_transportation_passenger_seat);
        tvLink = (TextView) rootView.findViewById(R.id.activity_public_transportation_link);
    }

    public void setInfo(final RailSegment reservation) {
        String departureTime = DateTimeHelper.getTime(reservation.getDepartureDatetime());
        String arrivalTime = DateTimeHelper.getTime(reservation.getArrivalDatetime());
        String departureDate = context.getString(R.string.departs).concat(" ").concat(DateTimeHelper.getDate(reservation.getDepartureDatetime()));
        String arrivalDate = context.getString(R.string.arrives).concat(" ").concat(DateTimeHelper.getDate(reservation.getArrivalDatetime()));
        //long flightDuration = reservation.getDuration();
        //String departureGate = reservation.getDeparture().getGate();
        //String arrivalGate = reservation.getArrival().getGate();
        tvTrainNumber.setText(reservation.getRailLine() + " " + reservation.getTrainNumber());
        tvDepartureStationName.setText(reservation.getOrigin());
        tvArrivalStationName.setText(reservation.getDestination());
        tvDepartureDate.setText(departureDate);
        tvDepartureTime.setText(departureTime);
        tvArrivalDate.setText(arrivalDate);
        tvArrivalTime.setText(arrivalTime);
        String name = "";
        if (reservation.getFirstName() != null && reservation.getLastName() != null) {
            name = reservation.getFirstName().concat(" ").concat(reservation.getLastName());
        }
        tvPassenger.setText(name);
       /* if (departureGate != null) {
            tvDepartureGate.setText(departureGate);
        }
        if (arrivalGate != null) {
            tvArrivalGate.setText(arrivalGate);
        }*/
        tvConfirmationNumber.setText(reservation.getConfirmationNo());
        tvSeat.setText(reservation.getSeatAssignment());
        tvTravelTicket.setText(reservation.getTicketNumber());
        //tvClassType.setText(reservation.getClassType());
        /*if (reservation.getBookingDetails() != null && reservation.getBookingDetails().getUrl() != null) {
            tvLink.setText(reservation.getBookingDetails().getUrl());
        }
        if (flightDuration == 0) {
            flightDuration = TimeHelper.getTravelDurationFromDepartureArrivalTimes(reservation.getDeparture().getUtcDateTime(), reservation.getArrival().getUtcDateTime());
        }
        tvTravelDuration.setText(TimeHelper.getDurationFromMinutes(context, (int) flightDuration));*/
    }

    public void showProgress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });
    }

    public void hideProgress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    public interface PublicTransportationViewListener {
        void onNavigateBack();
    }
}
