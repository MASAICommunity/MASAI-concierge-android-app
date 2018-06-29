package solutions.masai.masai.android.core.helper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Helper class for OkHttp client.
 */
public class OkHttpHelper {
    private static OkHttpClient httpClientForUploadFile;
    private static OkHttpClient httpClientForDownloadFile;
    private static OkHttpClient httpClientForWS;

    public static OkHttpClient getClientForUploadFile() {
        if (httpClientForUploadFile == null) {
            httpClientForUploadFile = new OkHttpClient.Builder()
                    .build();
        }
        return httpClientForUploadFile;
    }

    /**
     * acquire OkHttpClient instance for WebSocket connection.
     */
    public static OkHttpClient getClientForWebSocket() {
        if (httpClientForWS == null) {
            httpClientForWS = new OkHttpClient.Builder().readTimeout(0, TimeUnit.NANOSECONDS)
                    .build();
        }
        return httpClientForWS;
    }
}
