package solutions.masai.masai.android.hotel_reservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.DateTimeHelper;
import solutions.masai.masai.android.core.helper.general_base_controller.GeneralBaseView;
import solutions.masai.masai.android.core.model.journey.segment_types.HotelSegment;

/**
 * Created by Semko on 2016-12-05.
 */

public class HotelReservationView extends GeneralBaseView {
    private View rootView;
    private HotelReservationViewListener listener;
    private Context context;
    private Handler handler;
    private TextView tvTitle;
    private TextView tvCheckIn;
    private TextView tvCheckOut;
    private TextView tvAddress;
    private TextView tvHotelAddress;
    private TextView tvConfirmationNumber;
    private TextView tvPhone;
    private TextView tvMail;
    private ImageView ivBack;
    private ProgressDialog progressDialog;

    HotelReservationView(View rootView, Context context, HotelReservationViewListener listener) {
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
        tvCheckIn = (TextView) rootView.findViewById(R.id.hotel_check_in);
        tvCheckOut = (TextView) rootView.findViewById(R.id.hotel_check_out);
        tvAddress = (TextView) rootView.findViewById(R.id.hotel_detail);
        tvConfirmationNumber = (TextView) rootView.findViewById(R.id.hotel_confirmation_number);
        tvHotelAddress = (TextView) rootView.findViewById(R.id.hotel_address);
        tvMail = (TextView) rootView.findViewById(R.id.hotel_mail);
        tvPhone = (TextView) rootView.findViewById(R.id.hotel_phone);
    }

    public void setInfo(final HotelSegment reservation) {
        String checkInTime = DateTimeHelper.getDate(reservation.getCheckinDate());
        String checkOutTime = DateTimeHelper.getDate(reservation.getCheckoutDate());
        //int stayDuration = TimeHelper.getStayDuration(reservation.getCheckIn(), reservation.getCheckOut());
        //String stayDurationString = Integer.toString(stayDuration).concat(" ").concat(context.getString(R.string.days));
        tvCheckIn.setText(checkInTime);
        tvCheckOut.setText(checkOutTime);
        tvAddress.setText(setAddress(reservation));
        tvConfirmationNumber.setText(reservation.getConfirmationNo());
        tvHotelAddress.setText(reservation.getCityName());
        tvHotelAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMapClicked();
            }
        });
        /*tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPhoneClicked();
            }
        });
        tvPhone.setText(reservation.getPhone());*/
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

    public String setAddress(HotelSegment reservation){
        return reservation.getCityName() +System.getProperty("line.separator") + reservation.getAddress1() + System.getProperty("line.separator") + reservation.getPostalCode() + " " + reservation.getCountry();
    }

    public interface HotelReservationViewListener {
        void onMapClicked();

        void onPhoneClicked();

        void onNavigateBack();
    }
}
