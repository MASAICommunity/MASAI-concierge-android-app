package solutions.masai.masai.android.core.helper.network;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Sebastian Tanzer
 * @version 1.0
 *          created on 05/09/2017
 */

public class ConnectivityInterceptor implements Interceptor {

    private Context mContext;

    public ConnectivityInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        /*
        if (!NetworkUtil.isOnline(mContext)) {
            throw new NoConnectivityException();
            //throw new NoConnectivityException();
            //Toast.makeText(mContext, R.string.no_connection, Toast.LENGTH_LONG);
        }
        */
        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }


}