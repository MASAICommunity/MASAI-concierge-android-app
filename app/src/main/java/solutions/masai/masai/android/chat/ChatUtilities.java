package solutions.masai.masai.android.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.google.gson.Gson;
import solutions.masai.masai.android.BuildConfig;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.model.GooglePlace;
import solutions.masai.masai.android.core.model.GooglePlacesWrapper;
import solutions.masai.masai.android.core.model.Message;
import solutions.masai.masai.android.core.model.PermissionQuery;
import solutions.masai.masai.android.core.model.Train;
import solutions.masai.masai.android.core.model.TravelFolderPermissionResponse;
import solutions.masai.masai.android.core.model.User;
import solutions.masai.masai.android.present_image.PresentImageController;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * Created by Semko on 2017-02-06.
 */

public class ChatUtilities {

    public static void startMap(Context context, float latitude, float longitude, String name) {
        try {
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", latitude, longitude, name);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.app_not_found, Toast.LENGTH_LONG).show();
        }
    }

    static String getPathFromUri(Context context, Uri imageUri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(imageUri, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            return cursor.getString(columnIndex);
        }
        return null;
    }

    private static String getUserNameFromMessage(Message message) {
        if (message.getUser().getFullName() != null && !message.getUser().getFullName().isEmpty()) {
            return message.getUser().getFullName();
        } else if (message.getUser().getUsername() != null && !message.getUser().getUsername().isEmpty()) {
            return message.getUser().getUsername();
        } else if (message.getUser().getEmail() != null && !message.getUser().getEmail().isEmpty()) {
            return message.getUser().getEmail();
        } else {
            return "";
        }
    }

    public static String getAppropriateMessageSender(Message message) {
        if (message.isMine()) {
            User user = ConnectionManager.getInstance().getUser();
            if (user.getFullName() != null && !user.getFullName().isEmpty()) {
                return user.getFullName();
            } else if (user.getUsername() != null && !user.getUsername().isEmpty()) {
                return user.getUsername();
            } else if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                return user.getEmail();
            } else {
                return getUserNameFromMessage(message);
            }
        } else {
            return getUserNameFromMessage(message);
        }
    }

    @SuppressLint("NewApi")
    public static String getPathFromURI(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getMimeType(String url) {
        String type = null;
        String test = url.substring(url.lastIndexOf('.'));
        String extension = MimeTypeMap.getFileExtensionFromUrl(test);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        return type;
    }

    static String takePhoto(Activity activity) {
        String photoDir = getPhotoDir(activity);
        final File root = new File(photoDir);
        root.mkdirs();
        final String fname = "img_" + System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        Uri outputFileUri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputFileUri = FileProvider.getUriForFile(activity,
                BuildConfig.APPLICATION_ID + ".provider",
                sdImageMainDirectory);
        List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            activity.grantUriPermission(packageName, outputFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        String path = sdImageMainDirectory.getAbsolutePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        activity.startActivityForResult(intent, C.ACTION_IMAGE_SELECTED);
        return path;
    }

    static void selectImageFromGallery(Activity activity) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(Intent.createChooser(i, activity.getString(R.string.select_photo_source)), C.ACTION_IMAGE_SELECTED);
    }

    static void selectFile(Activity activity) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.select_photo_source)), C.ACTION_IMAGE_SELECTED);
    }

    private static String getPhotoDir(Context context) {
        String photoDir = context.getExternalFilesDir(null).getAbsolutePath().concat("/photos/");
        File imagesDir = new File(photoDir);
        if (!imagesDir.exists()) {
            imagesDir.mkdir();
        }
        return photoDir;
    }

    public static void loadFromFile(Context context, String path, ImageView imageView, RequestListener requestListener) {
        File file = new File(path);
        try {
            C.L("Loading image trying to load image from path " + path);
            if (requestListener != null) {
                Glide.with(context).load(file).error(R.drawable.ic_close).listener(requestListener).into(imageView);
            } else {
                Glide.with(context).load(file).error(R.drawable.ic_close).into(imageView);
            }
            imageView.setTag(R.string.image_view_tag_source_file, path);
        } catch (Exception iae) {
            C.L("Loading image couldn't load temporary image");
            iae.printStackTrace();
        }
    }

    public static void openPhotoPreview(Context context, View viewWithATag, int connectionPositionOnList) {
        Intent intent = new Intent(context, PresentImageController.class);
        intent.putExtra(C.EXTRA_HOST_CONNECTION_POSITION, connectionPositionOnList);
        if (viewWithATag.getTag(R.string.image_view_tag_source_file) != null) {
            intent.putExtra(context.getString(R.string.image_view_tag_source_file), (String) viewWithATag.getTag(R.string.image_view_tag_source_file));
            C.L("Opening image with FILE path = " + viewWithATag.getTag(R.string.image_view_tag_source_file));
        }
        if (viewWithATag.getTag(R.string.image_view_tag_source_url) != null) {
            intent.putExtra(context.getString(R.string.image_view_tag_source_url), (String) viewWithATag.getTag(R.string.image_view_tag_source_url));
            C.L("Opening image with FILE url = " + viewWithATag.getTag(R.string.image_view_tag_source_url));
        }
        context.startActivity(intent);
    }

    public static Train[] getTrains(String message) {
        try {
            return new Gson().fromJson(message, Train[].class);
        } catch (Exception e) {
            return null;
        }
    }

    public static GooglePlace[] getGooglePlaces(String message) {
        GooglePlacesWrapper wrapper;
        try {
            wrapper = new Gson().fromJson(message, GooglePlacesWrapper.class);
        } catch (Exception e) {
            return null;
        }
        if (wrapper != null) {
            return wrapper.getResults();
        }
        return null;
    }

    public static TravelFolderPermissionResponse getTravelFolderPermissionResponse(String message) {
        try {
            return new Gson().fromJson(message, TravelFolderPermissionResponse.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static PermissionQuery getPermissionQuery(String message) {
        try {
            return new Gson().fromJson(message, PermissionQuery.class);
        } catch (Exception e) {
            return null;
        }
    }

    public enum ShareType {
        CAMERA_TYPE, IMAGE_TYPE, FILE_TYPE, POSITION_TYPE
    }
}
