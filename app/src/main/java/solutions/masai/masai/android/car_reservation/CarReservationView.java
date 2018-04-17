package solutions.masai.masai.android.car_reservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.general_base_controller.GeneralBaseView;
import solutions.masai.masai.android.core.model.journey.segment_types.CarSegment;

import static solutions.masai.masai.android.core.helper.DateTimeHelper.getDateTime;

/**
 * Created by Semko on 2016-12-05.
 */

public class CarReservationView extends GeneralBaseView {
    private View rootView;
    private ActivityViewListener listener;
    private Context context;
    private Handler handler;
    private TextView tvCompany;
    private TextView tvTitle;
    private TextView tvCity;
    private TextView tvType;
    private TextView tvStart;
    private TextView tvStartAddress;
    private TextView tvEnd;
    private TextView tvEndAddress;
    private TextView tvConfirmationNumber;
    private ProgressDialog progressDialog;

    CarReservationView(View rootView, Context context, ActivityViewListener listener) {
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
        tvConfirmationNumber = (TextView) rootView.findViewById(R.id.activity_confirmation_number);
        tvCity = (TextView) rootView.findViewById(R.id.car_city);
        tvCompany = (TextView) rootView.findViewById(R.id.car_company);
        tvType = (TextView) rootView.findViewById(R.id.car_type);
        tvStart = (TextView) rootView.findViewById(R.id.car_start_date);
        tvStartAddress = (TextView) rootView.findViewById(R.id.car_start_address);
        tvEnd = (TextView) rootView.findViewById(R.id.car_end_date);
        tvEndAddress = (TextView) rootView.findViewById(R.id.car_end_address);
    }

    public void setInfo(final CarSegment reservation) {
        String begin = reservation.getPickupCityName()
                .concat(System.getProperty("line.separator"))
                .concat(reservation.getPickupAddress1())
                .concat(System.getProperty("line.separator"))
                .concat(reservation.getPickupPostalCode())
                .concat(" ")
                .concat(reservation.getPickupCountry());
        String end = reservation.getDropoffCityName()
                .concat(System.getProperty("line.separator"))
                .concat(reservation.getDropoffAddress1())
                .concat(System.getProperty("line.separator"))
                .concat(reservation.getDropoffPostalCode())
                .concat(" ")
                .concat(reservation.getDropoffCountry());

        tvStart.setText(getDateTime(reservation.getPickupDatetime()));
        tvEnd.setText(getDateTime(reservation.getDropoffDatetime()));

        tvStartAddress.setText(begin);
        tvEndAddress.setText(end);

        tvType.setText(reservation.getCarType());
        tvCity.setText(reservation.getPickupCityName());

        tvCompany.setText(reservation.getCarCompany());

        tvConfirmationNumber.setText(reservation.getConfirmationNo());

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

    public interface ActivityViewListener {
        void onNavigateBack();
    }
}
