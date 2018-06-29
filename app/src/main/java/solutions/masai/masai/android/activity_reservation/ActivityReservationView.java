package solutions.masai.masai.android.activity_reservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import solutions.masai.masai.android.R;

import solutions.masai.masai.android.core.helper.general_base_controller.GeneralBaseView;
import solutions.masai.masai.android.core.model.journey.segment_types.ActivitySegment;

import static solutions.masai.masai.android.core.helper.DateTimeHelper.getDateTime;

/**
 * Created by Semko on 2016-12-05.
 */

public class ActivityReservationView extends GeneralBaseView {
    private View rootView;
    private ActivityViewListener listener;
    private Context context;
    private Handler handler;
    private TextView tvTitle;
    private TextView tvCity;
    private TextView tvName;
    private TextView tvBegin;
    private TextView tvEnd;
    private TextView tvParticipant;
    private TextView tvConfirmationNumber;
    private ImageView ivBack;
    private ProgressDialog progressDialog;

    ActivityReservationView(View rootView, Context context, ActivityViewListener listener) {
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
        tvCity = (TextView) rootView.findViewById(R.id.activity_city);
        tvName = (TextView) rootView.findViewById(R.id.activity_name);
        tvBegin = (TextView) rootView.findViewById(R.id.activity_begin);
        tvEnd = (TextView) rootView.findViewById(R.id.activity_end);
        tvParticipant = (TextView) rootView.findViewById(R.id.activity_user);
    }

    public void setInfo(final ActivitySegment reservation) {
        String begin = getDateTime(reservation.getStartDatetime())
                .concat(System.getProperty("line.separator"))
                .concat(reservation.getStartCityName())
                .concat(System.getProperty("line.separator"))
                .concat(reservation.getStartAddress1())
                .concat(System.getProperty("line.separator"))
                .concat(reservation.getStartPostalCode())
                .concat(" ")
                .concat(reservation.getStartCountry());
        String end = getDateTime(reservation.getStartDatetime())
                .concat(System.getProperty("line.separator"))
                .concat(reservation.getEndCityName())
                .concat(System.getProperty("line.separator"))
                .concat(reservation.getEndAddress1())
                .concat(System.getProperty("line.separator"))
                .concat(reservation.getEndPostalCode())
                .concat(" ")
                .concat(reservation.getEndCountry());

        tvCity.setText(reservation.getStartCityName());
        tvName.setText(reservation.getActivityName());

        tvBegin.setText(begin);
        tvEnd.setText(end);

        tvParticipant.setText(reservation.getFirstName().concat(" ").concat(reservation.getLastName()));

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
