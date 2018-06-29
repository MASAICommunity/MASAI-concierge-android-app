package solutions.masai.masai.android.core.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

/**
 * Created by Semko on 2017-04-26.
 */

public class PermissionHelper {


    public static void showPermissionDialog(Context context, int stringId) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(null);
        builder.setMessage(stringId);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    @SuppressLint("NewApi")
    public static void showPermissionRationaleDialog(Context context, int stringId, DialogInterface.OnDismissListener dismissListener) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(null);
        builder.setMessage(stringId);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setOnDismissListener(dismissListener);
        builder.show();
    }

    public static void askForPermissionToReadExternalFiles(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, C.ACTION_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    public static void askForPermissionToWriteExternalFiles(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, C.ACTION_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    public static void askForPermissionToGetLocation(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, C.ACTION_PERMISSION_REQUEST_ACCESS_LOCATION);
        }
    }
}
