package solutions.masai.masai.android.core.helper;

import android.content.Context;

import java.util.UUID;

/**
 * Created by Semko on 2017-01-17.
 */

public class GCMHelper {
    private static final String KEY_PUSH_ID = "pushId";


    public static String getOrCreatePushId(Context context) {
        PrefsHelper prefsHelper = PrefsHelper.getInstance();
        if (!prefsHelper.contains(KEY_PUSH_ID)) {
            String newId = UUID.randomUUID().toString().replace("-", "");
            prefsHelper.setString(KEY_PUSH_ID, newId);
            return newId;
        }
        return prefsHelper.getString(KEY_PUSH_ID);
    }
}
