package solutions.masai.masai.android.core.helper.exception;

import java.io.IOException;

/**
 * @author Sebastian Tanzer
 * @version 1.0
 *          created on 05/09/2017
 */

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No Internet Connection!";
    }

}
