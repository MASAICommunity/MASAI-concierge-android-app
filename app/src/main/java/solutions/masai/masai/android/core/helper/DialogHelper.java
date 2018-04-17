package solutions.masai.masai.android.core.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import solutions.masai.masai.android.R;

/**
 * Created by Semko on 2017-02-21.
 */

public class DialogHelper {
    public static void dialogOk(Context context, int stringId) {
        new AlertDialog.Builder(context).setMessage(stringId).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    public static void dialogMessageOptionsDilemma(Context context, final OnMessageOptionsDialogCallback listener) {
        new AlertDialog.Builder(context).setMessage(R.string.message_options_content)
                .setPositiveButton(R.string.resend, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onResend();
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton(R.string.open, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onOpen();
                dialogInterface.dismiss();
            }
        }).show();
    }

    public static void dialogYesNo(Context context, int titleStringId, int messageStringId, final OnYesNoDialogCallback listener) {
        new AlertDialog.Builder(context).setTitle(titleStringId).setMessage(messageStringId)
                .setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onYes();
                dialogInterface.dismiss();
            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onNo();
                dialogInterface.dismiss();
            }
        }).show();
    }

    public static void dialogMap(Context context, OnMapReadyCallback mapReadyCallback, final OnMapDialogCallback mapDialogCallback) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_map);
        dialog.show();
        final MapView mapView = (MapView) dialog.findViewById(R.id.map_view_marker);
        Button cancel = (Button) dialog.findViewById(R.id.map_view_cancel);
        Button share = (Button) dialog.findViewById(R.id.map_view_share);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapDialogCallback.onShareLocation();
                dialog.dismiss();
            }
        });
        MapsInitializer.initialize(context);
        mapView.onCreate(dialog.onSaveInstanceState());
        mapView.onResume();
        mapView.getMapAsync(mapReadyCallback);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mapView.onStop();
                GeolocationProvider.getInstance().stop();
            }
        });
    }

    public interface OnMessageOptionsDialogCallback {
        void onResend();

        void onOpen();
    }

    public interface OnYesNoDialogCallback {
        void onYes();

        void onNo();
    }

    public interface OnMapDialogCallback {
        void onShareLocation();
    }
}
