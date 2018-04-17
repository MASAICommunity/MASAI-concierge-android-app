package solutions.masai.masai.android.core.helper;


import com.embiq.communicationmanager.utils.Log;

/**
 * Created by Semko on 2016-12-01.
 */

public class C {
    public static final String BASE_AWS_URL = "https://qsu2vaqmg2.execute-api.eu-central-1.amazonaws.com";
    public static final String ROCKETCHAT_ROOM_CLOSED_KEY = "promptTranscript";
    public static final String REFRESH_CONVERSATIONS_BY_NOTIFICATION = "RefreshConversationsByNotification";
    public static final String AUTHORIZATION_BEARER_PREFIX = "Bearer ";
    public static final String GOOGLE_MAPS_API_KEY = "AIzaSyDdl71H4yM-btDhq1Wzcbvwd7WlBkrT9Lw";
    public static final String RPC_LOGIN = "login";
    public static final String RPC_RESET_PASSWORD = "sendForgotPasswordEmail";
    public static final String RPC_GET_ROOMS = "subscriptions/get";
    public static final String RPC_LOAD_HISTORY = "loadHistory";
    public static final String RPC_SEND_MESSAGE = "sendMessage";
    public static final String RPC_CREATE_CHANNEL = "createChannel";
    public static final String RPC_REGISTER_USER = "registerUser";
    public static final String RPC_SET_USERNAME = "setUsername";
    public static final String RPC_JOIN_CHANNELS = "joinDefaultChannels";
    public static final String RPC_UPLOAD_REQUEST = "slingshot/uploadRequest";
    public static final String RPC_SEND_FILE_MESSAGE = "sendFileMessage";
    public static final String RPC_PUSH_UPDATE = "raix:push-update";
    public static final String RPC_SUBSCRIBE_TO_ROOM = "stream-room-messages";

    // json fields names
    public static final String JSON_FIELD_ID = "_id";
    public static final String JSON_FIELD_ROOM_ID = "rid";
    public static final String JSON_FIELD_MESSAGE = "msg";
    public static final String JSON_FIELD_SYNCSTATE = "syncstate";
    public static final String JSON_FIELD_USER_SHORT = "u";
    public static final String JSON_FIELD_LOCATION = "location";
    public static final String JSON_FIELD_ATTACHMENTS = "attachments";
    public static final String JSON_FIELD_USER = "user";
    public static final String JSON_FIELD_EMAIL = "email";
    public static final String JSON_FIELD_USERNAME = "username";
    public static final String JSON_FIELD_USER_ID = "_id";
    public static final String JSON_FIELD_LOCATION_TYPE = "type";
    public static final String JSON_FIELD_LOCATION_LAT = "lat";
    public static final String JSON_FIELD_LOCATION_LNG = "lng";
    public static final String JSON_FIELD_ARGS = "args";
    public static final String JSON_FIELD_NAME = "name";
    public static final String JSON_FIELD_SIZE = "size";
    public static final String JSON_FIELD_TYPE = "type";
    public static final String JSON_FIELD_PASSWORD = "password";
    public static final String JSON_FIELD_PASS = "pass";
    public static final String JSON_FIELD_CONFIRM_PASS = "confirm-pass";
    public static final String JSON_FIELD_DIGEST = "digest";
    public static final String JSON_FIELD_ALGORITHM = "algorithm";
    public static final String JSON_FIELD_SHA_256 = "sha-256";
    public static final String JSON_FIELD_TIMESTAMP = "ts";
    public static final String JSON_FIELD_POS_ON_THE_LIST = "connectionPositionOnList";
    public static final String JSON_FIELD_MESSAGES = "messages";
    public static final String JSON_FIELD_DATE = "$date";
    public static final String JSON_FIELD_TITLE = "title";
    public static final String JSON_FIELD_TITLE_URL = "title_url";

    public static final String JSON_VALUE_LOCATION_POINT = "Point";
    public static final String TITLE_VALUE_FILE = "TITLE_VALUE_FILE";

    //intent extras
    public static final String EXTRA_HOST_CONNECTION_POSITION = "EXTRA_HOST_CONNECTION_POSITION";
    public static final String EXTRA_HOTEL_RESERVATION = "EXTRA_HOTEL_RESERVATION";
    public static final String EXTRA_FLIGHT = "EXTRA_FLIGHT";
    public static final String EXTRA_CAR = "EXTRA_CAR";
    public static final String EXTRA_ACTIVITY = "EXTRA_ACTIVITY";
    public static final String EXTRA_TRAIN = "EXTRA_TRAIN";
    public static final String EXTRA_FLIGHT_INFO = "EXTRA_FLIGHT_INFO";
    public static final String EXTRA_ROOM_ID = "EXTRA_ROOM_ID";
    public static final String EXTRA_SHOW_PERMISSION_REQUEST = "EXTRA_SHOWPERMISSIONREQUEST";
    public static final String EXTRA_SEARCH_TERM = "EXTRA_SEARCH_TERM";

    public static final String FILE_FOLDER_ICON = ":file_folder:";

    public static final int ACTION_IMAGE_SELECTED = 666;
    public static final int ACTION_FILE_SELECTED = 313;
    public static final int ACTION_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 2;
    public static final int ACTION_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 4;
    public static final int ACTION_PERMISSION_REQUEST_ACCESS_LOCATION = 6;

    //app consts
    public static final int TIMEOUT_MS = 10000;
    public static final int MIN_TIME_DIFFERENCE = 180000;
    public static final int SEC_5 = 5000;
    private static final String GOOGLE_PLACES_API_KEY = "AIzaSyBlLXsEscJsJg2CrV8JGeiRFG7D9FaympE";
    public static final String GOOGLE_PLACES_PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?key=".concat(GOOGLE_PLACES_API_KEY).concat("&photoreference=%s&maxwidth=%d");
    public static final String HELP_UND_FEEDBACK_EMAIL = "office@mogree.com";
    /**
     * agreed with both sides
     */

    //prefs
    public static final String PREFERENCES_KEY_USER = "PREFERENCES_KEY_USER";
    public static final String PREFERENCES_MAP_USER_IDS_AND_ROOM_IDS = "PREFERENCES_MAP_USER_IDS_AND_ROOM_IDS";

    public static final String GOOGLE_APP_NAME = "db-concierge";

    //log
    private static final String LOG_TAG = "LOG_TAG";
    public static final String LOG_HOST = "Host ";

    public static void L(String log) {
        Log.e(LOG_TAG, log);
    }

    public static String COUNTRY_CODE;

    public static final String ROCKETCHAT_AVATAR_PARTIAL_ROUTE = "avatar/";
}
