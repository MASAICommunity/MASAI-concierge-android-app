package solutions.masai.masai.android.present_image;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.FileHelper;
import solutions.masai.masai.android.core.helper.PermissionHelper;

public class PresentImageController extends AppCompatActivity implements PresentImageView.PresentImageViewListener {
    private PresentImageView view;
    private String url;
    private String userId;
    private String token;
    private String imagePath;
    private int connectionPositionOnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_present_image);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view = new PresentImageView(findViewById(android.R.id.content), this, this);
        connectionPositionOnList = getIntent().getIntExtra(C.EXTRA_HOST_CONNECTION_POSITION, -1);
        if (connectionPositionOnList > -1) {
            if (getIntent().hasExtra(getString(R.string.image_view_tag_source_file))) {
                imagePath = getIntent().getStringExtra(getString(R.string.image_view_tag_source_file));
                Glide.with(this).load(imagePath).error(R.drawable.add_message).into(view.getPhotoView());
            } else if (getIntent().hasExtra(getString(R.string.image_view_tag_source_url))) {
                url = getIntent().getStringExtra(getString(R.string.image_view_tag_source_url));
                userId = ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost().getRcUser()
                        .getUserId();
                token = ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost().getRcUser().getUserId();
                GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                        .addHeader("Cookie", "rc_uid=" + userId + ";rc_token=" + token)
                        .build());
                Glide.with(this).load(glideUrl).error(R.drawable.add_message).into(view.getPhotoView());
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case C.ACTION_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PermissionHelper.askForPermissionToWriteExternalFiles(this);
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        PermissionHelper.showPermissionRationaleDialog(this, R.string.please_give_us_access_to_your_files, new DialogInterface.OnDismissListener() {
                            public void onDismiss(DialogInterface dialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    PermissionHelper.askForPermissionToReadExternalFiles(PresentImageController.this);
                                }
                            }
                        });
                    } else {
                        PermissionHelper.showPermissionDialog(this, R.string.no_access_to_files_info);
                    }
                }
            }
            case C.ACTION_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadOrOpenFile();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        PermissionHelper.showPermissionRationaleDialog(this, R.string.please_give_us_access_to_write_your_files, new DialogInterface.OnDismissListener() {
                            public void onDismiss(DialogInterface dialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    PermissionHelper.askForPermissionToWriteExternalFiles(PresentImageController.this);
                                }
                            }
                        });
                    } else {
                        PermissionHelper.showPermissionDialog(this, R.string.no_access_to_write_files_info);
                    }
                }
            }
        }
    }

    private void downloadOrOpenFile() {
        C.L("Trying to download image ");
        if (url != null && userId != null && token != null) {
            if (FileHelper.downloadOrOpenFileFromUrl(this, url, userId, token)) {
                Toast.makeText(this, R.string.download_started, Toast.LENGTH_LONG).show();
            }
        } else if (imagePath != null) {
            FileHelper.openFile(this, imagePath);
        }
    }

    @Override
    public void onClose() {
        onBackPressed();
    }

    @Override
    public void onDownload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionHelper.askForPermissionToReadExternalFiles(this);
        } else {
            downloadOrOpenFile();
        }
    }
}