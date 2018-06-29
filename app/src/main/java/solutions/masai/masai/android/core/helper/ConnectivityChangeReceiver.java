package solutions.masai.masai.android.core.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Semko on 2017-05-04.
 */

public class ConnectivityChangeReceiver extends BroadcastReceiver {
    private String tag = "El Diablo";
    private INetworkState listener;

    public ConnectivityChangeReceiver(INetworkState listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                if (key.contains("workI")) {
                    NetworkInfo info = (NetworkInfo) extras.get(key);
                    Log.e(tag, "TYPE = " + info.getTypeName() + ", " + info.getState());
                    if (info != null && info.getState().name().equals("CONNECTED")) {
                        listener.onStateConnected();
                    } else if(info != null && info.getState().name().equals("DISCONNECTED")) {
                        listener.onStateDisconnected();
                    }
                }
            }
        } else {
            Log.e(tag, "no extras");
        }
    }

    public static ConnectivityChangeReceiver registerNetworkStateReceiver(Context context, ConnectivityChangeReceiver.INetworkState listener) {
        ConnectivityChangeReceiver connectivityChangeReceiver = new ConnectivityChangeReceiver(listener);
        context.registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return connectivityChangeReceiver;
    }

    public interface INetworkState {
        void onStateConnected();

        void onStateDisconnected();
    }
}
