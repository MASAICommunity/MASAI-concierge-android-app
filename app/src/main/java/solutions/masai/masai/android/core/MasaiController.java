package solutions.masai.masai.android.core;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Semko on 2017-05-25.
 */

public class MasaiController extends AppCompatActivity {
    private Handler handler = new Handler(Looper.getMainLooper());

    public Handler getHandler() {
        return handler;
    }
}
