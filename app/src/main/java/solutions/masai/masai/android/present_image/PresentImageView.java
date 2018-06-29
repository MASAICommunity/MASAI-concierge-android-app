package solutions.masai.masai.android.present_image;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import solutions.masai.masai.android.R;

import uk.co.senab.photoview.PhotoView;

public class PresentImageView {
    private View rootView;
    private PresentImageViewListener listener;
    private Context context;
    private ProgressDialog progressDialog;
    private Handler handler;
    private ImageView ivDownload;
    private ImageView ivClose;
    private PhotoView pvPresent;

    PresentImageView(View rootView, Context context, PresentImageViewListener listener) {
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
        ivDownload = (ImageView) rootView.findViewById(R.id.present_image_download);
        ivClose = (ImageView) rootView.findViewById(R.id.present_image_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClose();
            }
        });
        pvPresent = (PhotoView) rootView.findViewById(R.id.present_image_photo_view);
        ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDownload();
            }
        });
    }

    public PhotoView getPhotoView() {
        return pvPresent;
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

    interface PresentImageViewListener {
        void onClose();

        void onDownload();
    }
}