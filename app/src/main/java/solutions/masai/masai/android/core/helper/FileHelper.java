package solutions.masai.masai.android.core.helper;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.net.URLConnection;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Semko on 2017-03-09.
 */

public class FileHelper {
    public static boolean downloadOrOpenFileFromUrl(Context context, String fileUrl, String userId, String token) {
        File mainDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String fileSegments[] = fileUrl.split("/");
        String fileName = fileSegments[fileSegments.length - 1];
        String dirPath = mainDir.getAbsolutePath().concat("/MASAI");
        File masaiPictureDirectory = new File(dirPath);
        masaiPictureDirectory.mkdirs();
        File attachmentFile = new File(masaiPictureDirectory, fileName);
        if (attachmentFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.fromFile(attachmentFile), URLConnection.guessContentTypeFromName(fileName));
            Uri apkURI = FileProvider.getUriForFile(
                    context,
                    context.getApplicationContext()
                            .getPackageName() + ".provider", attachmentFile);
            intent.setDataAndType(apkURI, URLConnection.guessContentTypeFromName(fileName));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } else {
            downloadFile(context, fileUrl, Uri.parse(attachmentFile.toURI().toString()), userId, token);
            return true;
        }
        return false;
    }

    public static void openFile(Context context, String filePath) {
        File file = new File(filePath);
        String fileSegments[] = filePath.split("/");
        String fileName = fileSegments[fileSegments.length - 1];
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), URLConnection.guessContentTypeFromName(filePath));
        context.startActivity(intent);
    }

    private static void downloadFile(Context context, String fileUrl, Uri destinationUri, String userId, String token) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl))
                .setDestinationUri(destinationUri)
                .setVisibleInDownloadsUi(true)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                .addRequestHeader("Cookie", "rc_uid=" + userId + ";rc_token=" + token)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);
    }
}
