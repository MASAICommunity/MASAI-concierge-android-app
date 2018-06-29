package solutions.masai.masai.android.core.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashSet;
import java.util.Set;

public class PrefsHelper {
    private final static String DEFAULT_STRING_VALUE = "";
    private final static boolean DEFAULT_BOOLEAN_VALUE = false;
    private final static int DEFAULT_INTEGER_VALUE = 0;
    private final static long DEFAULT_LONG_VALUE = 0;
    private final static float DEFAULT_FLOAT_VALUE = 1.0f;
    private static SharedPreferences prefs;
    private static PrefsHelper instance = null;

    private PrefsHelper(Context ctx) {
        prefs = ctx.getApplicationContext().getSharedPreferences(getClass().getPackage().toString(), Context.MODE_PRIVATE);
    }

    public static PrefsHelper init(Context ctx) {
        if (instance == null)
            instance = new PrefsHelper(ctx);
        return instance;
    }

    /**
     * be aware that you can get null from this method !
     */
    public static PrefsHelper getInstance() {
        return instance;
    }

    public String getString(String key) {
        return prefs.getString(key, DEFAULT_STRING_VALUE);
    }

    public String getString(String key, String customDefaultValue) {
        return prefs.getString(key, customDefaultValue);
    }

    public void setString(String key, String value) {
        Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public Set<String> getStringSet(String key) {
        return prefs.getStringSet(key, new HashSet<String>());
    }

    public void setStringSet(String key, Set<String> value) {
        Editor editor = prefs.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return prefs.getBoolean(key, DEFAULT_BOOLEAN_VALUE);
    }

    public void setBoolean(String key, boolean value) {
        Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public int getInt(String key, int defValue) {
        return prefs.getInt(key, defValue);
    }

    public int getInt(String key) {
        return prefs.getInt(key, DEFAULT_INTEGER_VALUE);
    }

    public void setInt(String key, int value) {
        Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public long getLong(String key, long defValue) {
        return prefs.getLong(key, defValue);
    }

    public long getLong(String key) {
        return prefs.getLong(key, DEFAULT_LONG_VALUE);
    }

    public void setLong(String key, long value) {
        Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public float getFloat(String key, float defValue) {
        return prefs.getFloat(key, defValue);
    }

    public float getFloat(String key) {
        return prefs.getFloat(key, DEFAULT_FLOAT_VALUE);
    }

    public void setFloat(String key, float value) {
        Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public boolean contains(String key) {
        return prefs.contains(key);
    }

    public void removePref(String key) {
        Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clearPreferences() {
        Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
