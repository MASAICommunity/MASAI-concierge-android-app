package solutions.masai.masai.android.flight_reservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import solutions.masai.masai.android.R;

import solutions.masai.masai.android.core.helper.general_base_controller.GeneralBaseView;
import solutions.masai.masai.android.core.model.journey.segment_types.AirSegment;

import static solutions.masai.masai.android.core.helper.DateTimeHelper.getDate;
import static solutions.masai.masai.android.core.helper.DateTimeHelper.getTime;
import static solutions.masai.masai.android.core.helper.DateTimeHelper.getTravelDurationFromDepartureArrivalTimes;

public class FlightReservationView extends GeneralBaseView{
    private View rootView;
    private FlightReservationViewListener listener;
    private Context context;
    private Handler handler;
    private TextView tvTitle;
    private TextView tvAirhost;
    private TextView tvDepartureAirportCode;
    private TextView tvArrivalAirportCode;
    private TextView tvDepartureDate;
    private TextView tvDepartureTime;
    private TextView tvArrivalDate;
    private TextView tvArrivalTime;
    private TextView tvDepartureTerminal;
    private TextView tvDepartureGate;
    private TextView tvArrivalTerminal;
    private TextView tvArrivalGate;
    private TextView tvTicketNumber;
    private TextView tvConfirmationNumber;
    private TextView tvPassengerName;
    private TextView tvSeat;
    private ImageView ivBack;
    private ProgressDialog progressDialog;

    FlightReservationView(View rootView, Context context, FlightReservationViewListener listener) {
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
        tvAirhost = (TextView) rootView.findViewById(R.id.activity_flight_airhost);
        tvArrivalAirportCode = (TextView) rootView.findViewById(R.id.activity_flight_arrival_airport_code);
        tvDepartureAirportCode = (TextView) rootView.findViewById(R.id.activity_flight_departure_airport_code);
        tvDepartureDate = (TextView) rootView.findViewById(R.id.activity_flight_departure_date);
        tvDepartureTime = (TextView) rootView.findViewById(R.id.activity_flight_departure_time);
        tvDepartureTerminal = (TextView) rootView.findViewById(R.id.activity_flight_departure_terminal);
        tvDepartureGate = (TextView) rootView.findViewById(R.id.activity_flight_departure_gate);
        tvArrivalDate = (TextView) rootView.findViewById(R.id.activity_flight_arrival_date);
        tvArrivalTime = (TextView) rootView.findViewById(R.id.activity_flight_arrival_time);
        tvArrivalTerminal = (TextView) rootView.findViewById(R.id.activity_flight_arrival_terminal);
        tvArrivalGate = (TextView) rootView.findViewById(R.id.activity_flight_arrival_gate);
        tvTicketNumber = (TextView) rootView.findViewById(R.id.activity_flight_duration);
        tvConfirmationNumber = (TextView) rootView.findViewById(R.id.activity_confirmation_number);
        tvPassengerName = (TextView) rootView.findViewById(R.id.activity_flight_passenger_name);
        tvSeat = (TextView) rootView.findViewById(R.id.activity_flight_seat);
    }

    public void setInfo(final AirSegment reservation) {
        String departureTime = getTime(reservation.getDepartureDatetime());
        String arrivalTime = getTime(reservation.getArrivalDatetime());
        String departureDate = context.getString(R.string.departs).concat(" ").concat(getDate(reservation.getDepartureDatetime()));
        String arrivalDate = context.getString(R.string.arrives).concat(" ").concat(getDate(reservation.getArrivalDatetime()));
        long flightDuration = 0;
        String departureGate = "GATE";
        String arrivalGate = "GATE2";
        String departureTerminal = "TERMINAL";
        String arrivalTerminal = "TERMINAL2";
        String airhost = "";
        if (reservation.getAirline() != null) {
            airhost = reservation.getAirline().concat(" ").concat(reservation.getFareBasisCode());
        }
        tvAirhost.setText(airhost);
        tvArrivalAirportCode.setText(reservation.getDestination());
        tvDepartureDate.setText(departureDate);
        tvDepartureTime.setText(departureTime);
        tvConfirmationNumber.setText(reservation.getConfirmationNo());
        tvDepartureAirportCode.setText(reservation.getDestinationAdminCode());
        String passengerName = "";
        if (reservation.getTravelers() != null) {
            passengerName = reservation.getTravelers().get(0).getName();
        }
        tvPassengerName.setText(passengerName);
        tvSeat.setText(reservation.getSeatAssignment());
        if (flightDuration == 0) {
            flightDuration = getTravelDurationFromDepartureArrivalTimes(reservation.getDepartureDatetime(), reservation.getArrivalDatetime(), null);
        }
        tvTicketNumber.setText(reservation.getTicketNumber());
        if (departureGate != null && !departureGate.isEmpty()) {
            tvDepartureGate.setText(departureGate);
        } else {
            tvDepartureGate.setText("-");
        }
        if (departureTerminal != null && !departureTerminal.isEmpty()) {
            tvDepartureTerminal.setText(departureTerminal);
        } else {
            tvDepartureTerminal.setText("-");
        }
        tvArrivalDate.setText(arrivalDate);
        tvArrivalTime.setText(arrivalTime);
        tvArrivalAirportCode.setText(reservation.getOrigin());
        if (arrivalGate != null && !arrivalGate.isEmpty()) {
            tvArrivalGate.setText(arrivalGate);
        } else {
            tvArrivalGate.setText("-");
        }
        if (arrivalTerminal != null && !arrivalTerminal.isEmpty()) {
            tvArrivalTerminal.setText(arrivalTerminal);
        } else {
            tvArrivalTerminal.setText("-");
        }
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

    public interface FlightReservationViewListener {
        void onNavigateBack();
    }
}
