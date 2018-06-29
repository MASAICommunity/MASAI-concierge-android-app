package solutions.masai.masai.android.core.helper;

/**
 * Created by Semko on 2016-12-08.
 */

public class RegexHelper {

    public static String simplifyDate(String json) {
        String regex = "\\{\\\"\\$date\":(\\d+)\\}";
        return json.replaceAll(regex, "$1");
    }

    public static String simplifyLocation(String json) {
        String regex = "\"coordinates\":\\[(\\d+\\.\\d+)\\D+(\\d+\\.\\d+)]";
        return json.replaceAll(regex, "\"lat\":$2,\"lng\":$1");
    }
}
