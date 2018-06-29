package solutions.masai.masai.android.core.communication;

import android.os.Handler;
import android.widget.TextView;

import com.embiq.communicationmanager.CommunicationManager;
import com.embiq.communicationmanager.enums.EHttpRequestMethod;
import com.embiq.communicationmanager.interfaces.IConnection;
import com.embiq.communicationmanager.requests.data.HttpRequestData;
import com.embiq.communicationmanager.responses.GenericObjResponseListener;
import com.embiq.communicationmanager.responses.StringHttpResponseListener;
import com.embiq.communicationmanager.settings.HttpConnectionSettings;
import com.google.gson.Gson;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.LocationHelper;
import solutions.masai.masai.android.core.model.GoogleMapsAddress;
import solutions.masai.masai.android.core.model.PermissionQuery;
import solutions.masai.masai.android.core.model.UserInfo;
import solutions.masai.masai.android.core.model.WebPageMeta;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import bolts.Task;

/**
 * Created by Semko on 2016-12-14.
 */

public class CustomRequests {
    private static final String LOTUS_REQUEST = "reisebuddy-api/incoming/lotusMail";
    private static final String WHAT_ABOUT_ME_REQUEST = "api/v1/me";
    private static final String UPDATE_USER = "api/v1/user.update";
    private static final String JSON_FIELD_BODY = "body";
    private static final String JSON_FIELD_SENDER = "sender";
    private static final String JSON_FIELD_SUBJECT = "subject";
    private static final String JSON_FIELD_NAME = "name";
    private static final String JSON_FIELD_PASSWORD = "password";
    private static final String JSON_FIELD_USER_ID = "userId";
    private static final String JSON_FIELD_USER_DATA = "data";
    private static final String HEADER_AUTHORIZATION_MASAI = "Authorization";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_CONTENT_TYPE_VALUE = "application/json";
    public static final String HEADER_AUTHORIZATION_ROCKET_CHAT = "X-Auth-Token";
    public static final String HEADER_USER_ROCKET_CHAT = "X-User-Id";
    private static final String HEADER_AUTHORIZATION_MASAI_VALUE = "Basic cm9ja2V0Y2hhdDpyb2NrZXRjaGF0MTIz";
    private static final String HEADER_LOCATION = "Location";

    private static IConnection getMasaiCustomConnection(String hostUrl) {
        IConnection connection = CommunicationManager.getConnection(hostUrl);
        if (connection == null) {
            try {
                HttpConnectionSettings connectionSettings = new HttpConnectionSettings(hostUrl);
                connectionSettings.addHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE);
                connectionSettings.addHeader(HEADER_AUTHORIZATION_MASAI, HEADER_AUTHORIZATION_MASAI_VALUE);
                connection = CommunicationManager.createConnection(hostUrl, connectionSettings);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    static void sendMessage(final String hostUrl, final String message, final String roomId, final SimpleHttpResponseListener listener) {
        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    C.L("trying to send message@2" + hostUrl);
                    IConnection connection = getMasaiCustomConnection(hostUrl);
                    connection = updateConnection(connection, hostUrl, null, null);
                    final JSONObject dataJson = new JSONObject().put(JSON_FIELD_BODY, message).put(JSON_FIELD_SENDER, roomId).put(JSON_FIELD_SUBJECT, "");
                    HttpRequestData data = new HttpRequestData(dataJson, LOTUS_REQUEST);
                    C.L("Trying to Send Message to " + hostUrl.concat(LOTUS_REQUEST) + " with data " + dataJson.toString());
                    connection.send(data, new StringHttpResponseListener() {
                        @Override
                        public void onHeadersReceived(Map<String, List<String>> headers) {

                        }

                        @Override
                        public void onSuccess(int responseCode, String responseData) {
                            C.L("Send Message to " + hostUrl.concat(LOTUS_REQUEST) + " with data " + dataJson.toString() + "returned" + Integer.toString(responseCode) + ", " + responseData);
                            listener.onSuccess();
                        }

                        @Override
                        public void onFail(int responseCode, String responseErrorData) {
                            C.L("Send Message request returned: Failed " + Integer.toString(responseCode) + ", " + responseErrorData);
                            listener.onFail();
                        }

                        @Override
                        public void onError(int errorCode, String responseErrorMessage) {
                            C.L("Send Message request returned: Error " + Integer.toString(errorCode) + ", " + responseErrorMessage);
                            listener.onFail();
                        }
                    });
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                return null;
            }
        });
    }

    public static void grantAccessToTravelFolder(PermissionQuery permissionQuery) {
        String token = ConnectionManager.getInstance().getUser().getIdToken();
        IConnection connection = CommunicationManager.getConnection("TravelFolderAccess");
        if (connection == null) {
            HttpConnectionSettings connectionSettings = new HttpConnectionSettings(permissionQuery.getUrl());
            try {
                connection = CommunicationManager.createConnection("TravelFolderAccess", connectionSettings);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String payload = new Gson().toJson(permissionQuery.getPayload());
        HttpRequestData requestData = new HttpRequestData(payload, "");
        requestData.setMethod(EHttpRequestMethod.POST);
        requestData.addHeader("Authorization", C.AUTHORIZATION_BEARER_PREFIX + token);
        requestData.addHeader("Content-Type", "application/json");
        connection.send(requestData, new StringHttpResponseListener() {
            @Override
            public void onHeadersReceived(Map<String, List<String>> map) {

            }

            @Override
            public void onSuccess(int i, String s) {
                C.L("grantAccessToTravelFolder success" + s);
            }

            @Override
            public void onFail(int i, String s) {
                C.L("grantAccessToTravelFolder fail" + s);
            }

            @Override
            public void onError(int i, String s) {
                C.L("grantAccessToTravelFolder error" + s);
            }
        });
    }

    private static void getRequest(String url, StringHttpResponseListener listener) {
        IConnection connection = CommunicationManager.getConnection(url);
        if (connection == null) {
            HttpConnectionSettings connectionSettings = new HttpConnectionSettings(url);
            try {
                connection = CommunicationManager.createConnection(url, connectionSettings);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        HttpRequestData requestData = new HttpRequestData("");
        requestData.setMethod(EHttpRequestMethod.GET);
        connection.send(requestData, listener);
    }

    public static void getWebPage(List<String> urls, final IPareInfoAboutPage listener) {
        final List<WebPageMeta> urlInfos = new ArrayList<>();
        for (final String url : urls) {
            C.L("WebPageMeta url = " + url);
            getRequest(url, new StringHttpResponseListener() {
                @Override
                public void onHeadersReceived(Map<String, List<String>> map) {
                    if (map.containsKey(HEADER_LOCATION)) {
                        if (map.get(HEADER_LOCATION).size() > 0) {
                            String redirectLocation = map.get(HEADER_LOCATION).get(0);
                            C.L("WebPageMeta" + url + " redirected to " + redirectLocation);
                            getRequest(redirectLocation, this);
                        }
                    }
                }

                @Override
                public void onSuccess(int i, String s) {
                    if (i == 200) {
                        WebPageMeta webPageMeta = extractAllText(s);
                        if (webPageMeta != null) {
                            urlInfos.add(webPageMeta);
                            JSONArray array = new JSONArray();
                            String joInfo = new Gson().toJson(webPageMeta, WebPageMeta.class);
                            array.put(joInfo);
                            String jsonString = array.toString();
                            C.L("WebPageMetaJSONJSON " + jsonString);
                        }
                    } else {
                        listener.onPageParseError();
                    }
                }

                @Override
                public void onFail(int i, String s) {
                    C.L("WebPageMeta" + url + " failed " + s);
                    listener.onPageParseError();
                }

                @Override
                public void onError(int i, String s) {
                    C.L("WebPageMeta" + url + " failed " + s);
                    listener.onPageParseError();
                }
            });
        }
    }

    public static WebPageMeta extractAllText(String htmlText) {
        Source source = new Source(htmlText);
        List<Element> elements = source.getAllElements("meta");
        WebPageMeta webPageMeta = new WebPageMeta();
        for (Element element : elements) {
            final String name = element.getAttributeValue("property");
            if (name != null) {
                String content = element.getAttributeValue("content");
                if (content != null && !content.isEmpty()) {
                    if (name.equals("og:image")) {
                        webPageMeta.setImageUrl(content);
                    }
                    if (name.equals("og:url")) {
                        webPageMeta.setUrl(content);
                    }
                    if (name.equals("og:title")) {
                        webPageMeta.setTitle(content);
                    }
                    if (name.equals("og:description")) {
                        webPageMeta.setDescription(content);
                    }
                }
            }
        }
        return webPageMeta;
    }

    public interface IPareInfoAboutPage {
        void onPageParsed(WebPageMeta webPageMeta);

        void onPageParseError();
    }

    public static void getAddressFromCoordinates(final float lat, final float lng, final TextView textView, final Handler handler) {
        String locationAddress = LocationHelper.getLocation(lat, lng);
        if (locationAddress != null) {
            textView.setText(locationAddress);
        } else {
            IConnection connection = CommunicationManager.getConnection("googlemaps");
            if (connection == null) {
                String url = "http://maps.googleapis.com/maps/api/geocode/json?";
                HttpConnectionSettings connectionSettings = new HttpConnectionSettings(url);
                try {
                    connection = CommunicationManager.createConnection("googlemaps", connectionSettings);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String coordinates = "latlng=" + Float.toString(lat) + "," + Float.toString(lng);
            HttpRequestData requestData = new HttpRequestData(coordinates);
            connection.send(requestData, new GenericObjResponseListener<GoogleMapsAddress>(GoogleMapsAddress.class) {
                @Override
                public void onSuccess(int i, GoogleMapsAddress googlePlacesAddress) {
                    String address = "";
                    if (googlePlacesAddress != null) {
                        if (googlePlacesAddress.getResults().length > 0) {
                            address = googlePlacesAddress.getResults()[0].getFormattedAddress();
                        }
                    }
                    final String finalAddress = address;
                    LocationHelper.addLocation(lat, lng, finalAddress);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(finalAddress);
                        }
                    });
                    C.L("Get address from coordinates succeeded  ");
                }

                @Override
                public void onFail(int i, String o) {
                    C.L("Get address from coordinates failed with " + o);
                }

                @Override
                public void onError(int i, String s) {
                    C.L("Get address from coordinates failed with " + s);
                }
            });
        }
    }

    private static IConnection updateConnection(IConnection connection, String hostURL, String hostToken, String userId) {
        HttpConnectionSettings connectionSettings = new HttpConnectionSettings(hostURL);
        connectionSettings.addHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE);
        connectionSettings.addHeader(HEADER_AUTHORIZATION_MASAI, HEADER_AUTHORIZATION_MASAI_VALUE);
        if (hostToken != null && !hostToken.isEmpty()) {
            connectionSettings.addHeader(HEADER_AUTHORIZATION_ROCKET_CHAT, hostToken);
        }
        if (userId != null && !userId.isEmpty()) {
            connectionSettings.addHeader(HEADER_USER_ROCKET_CHAT, userId);
        }
        connection.setNewConnectionSettings(connectionSettings);
        return connection;
    }

    static void getInfoAboutMe(final String hostUrl, final String hostToken, final String userId, final SimpleHttpResponseListener listener) {
        IConnection connection = getMasaiCustomConnection(hostUrl);
        connection = updateConnection(connection, hostUrl, hostToken, userId);
        HttpRequestData data = new HttpRequestData(WHAT_ABOUT_ME_REQUEST);
        C.L("QWER " + hostUrl);
        connection.send(data, new StringHttpResponseListener() {
            @Override
            public void onHeadersReceived(Map<String, List<String>> headers) {

            }

            @Override
            public void onSuccess(int responseCode, String responseData) {
                C.L("Send Message to " + hostUrl.concat(WHAT_ABOUT_ME_REQUEST) + " returned " + Integer.toString(responseCode) + ", " + responseData);
                UserInfo userInfo = new Gson().fromJson(responseData, UserInfo.class);
                ConnectionManager.getInstance().getUser().setEmail(userInfo.getEmail());
                ConnectionManager.getInstance().getUser().setUsername(userInfo.getUsername());
                listener.onSuccess();
            }

            @Override
            public void onFail(int responseCode, String responseErrorData) {
                C.L("Send Message to " + hostUrl.concat(WHAT_ABOUT_ME_REQUEST) + " returned " + Integer.toString(responseCode) + ", " + responseErrorData);
                listener.onFail();
            }

            @Override
            public void onError(int errorCode, String responseErrorMessage) {
                C.L("Send Message to " + hostUrl.concat(WHAT_ABOUT_ME_REQUEST) + " returned " + Integer.toString(errorCode) + ", " + responseErrorMessage);
                listener.onFail();
            }
        });
    }

    public static void updateMyAccount(final String hostUrl, final String hostToken, final String userId, final String newName, final String newPass, final SimpleHttpResponseListener listener) {
        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                IConnection connection = getMasaiCustomConnection(hostUrl);
                connection = updateConnection(connection, hostUrl, hostToken, userId);
                try {
                    final JSONObject dataJson = new JSONObject().put(JSON_FIELD_USER_ID, userId).put(JSON_FIELD_USER_DATA, new JSONObject().put(JSON_FIELD_NAME, newName).put(JSON_FIELD_PASSWORD, newPass));
                    HttpRequestData data = new HttpRequestData(dataJson, UPDATE_USER);
                    C.L("QWER " + hostUrl);
                    connection.send(data, new StringHttpResponseListener() {
                        @Override
                        public void onHeadersReceived(Map<String, List<String>> headers) {

                        }

                        @Override
                        public void onSuccess(int responseCode, String responseData) {
                            C.L("Send Message to " + hostUrl.concat(UPDATE_USER) + " returned " + Integer.toString(responseCode) + ", " + responseData);

                            listener.onSuccess();
                        }

                        @Override
                        public void onFail(int responseCode, String responseErrorData) {
                            C.L("Send Message to " + hostUrl.concat(UPDATE_USER) + " returned " + Integer.toString(responseCode) + ", " + responseErrorData);
                            listener.onFail();
                        }

                        @Override
                        public void onError(int errorCode, String responseErrorMessage) {
                            C.L("Send Message to " + hostUrl.concat(UPDATE_USER) + " returned " + Integer.toString(errorCode) + ", " + responseErrorMessage);
                            listener.onFail();
                        }
                    });
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                return null;
            }
        });
    }

    public interface SimpleHttpResponseListener {
        void onSuccess();

        void onFail();
    }
}
