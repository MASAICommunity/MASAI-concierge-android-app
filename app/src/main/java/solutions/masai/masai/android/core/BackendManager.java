package solutions.masai.masai.android.core;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import solutions.masai.masai.android.BuildConfig;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.network.ConnectivityInterceptor;
import solutions.masai.masai.android.core.helper.JourneyConverter;
import solutions.masai.masai.android.core.helper.TravelfolderUserConverter;
import solutions.masai.masai.android.core.model.journey.Journey;
import solutions.masai.masai.android.core.model.travelfolder_user.TravelfolderUser;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by cWahl on 21.08.2017.
 */

public class BackendManager {

	private static BackendManager instance = null;
	private static Retrofit retrofit = null;
	private OkHttpClient client;
	private BackendInterface backendService;

	private static Context context;

	private BackendManager() {
		buildRetrofit();
	}

	public static BackendManager getInstance() {
		if (instance == null) {
			instance = new BackendManager();
		}

		return instance;
	}

	private void buildRetrofit() {
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
				.addInterceptor(new ConnectivityInterceptor(context))
				.connectTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30L, TimeUnit.SECONDS)
				.writeTimeout(30L, TimeUnit.SECONDS);

		if (BuildConfig.DEBUG) {
			okHttpBuilder.addInterceptor(loggingInterceptor);
		}

		client = okHttpBuilder.build();

		retrofit = new Retrofit.Builder().baseUrl(C.BASE_AWS_URL)
				.addConverterFactory(buildGsonConverter())
				.client(client)
				.build();

		backendService = retrofit.create(BackendInterface.class);
	}

	public BackendInterface getBackendService() {
		return this.backendService;
	}

	public static void setContext(Context c) {
		context = c;
	}

	public interface BackendInterface {
		//@POST("https://requestb.in/17u486h1")
		@POST("/latest/users/me/profile")
		Call<ResponseBody> updateTravelfolderUser(@Body TravelfolderUser user, @Header("Authorization") String authHeader);

		@GET("/latest/users/me/profile")
		Call<TravelfolderUser> getTravelfolderUser(@Header("Authorization") String authHeader);

        @GET("/latest/choices")
        Call<ResponseBody> getChoices();

		@GET("/latest/users/me/access/grants")
		Call<ResponseBody> getGrantedUsers(@Header("Authorization") String authHeader);

		@DELETE("/latest/users/me/access/grants/{userId}")
		Call<ResponseBody> deleteGrantedUser(@Header("Authorization") String authHeader, @Path("userId") String userId);

		@POST("/latest/users/me/transactions")
		Call<ResponseBody> createTransaction(@Header("Authorization") String authHeader);

		@GET("/latest/users/me/journey-list")
		Call<List<Journey>> getJourneys(@Header("Authorization") String authHeader,
		                                @Query("filter-time") Integer time,
		                                @Query("filter-text") String text);

		@GET("/latest/users/me/hosts")
		Call<ResponseBody> getHostList(@Header("Authorization") String authHeader);
	}

	private static GsonConverterFactory buildGsonConverter() {

		GsonBuilder gsonBuilder = new GsonBuilder();
		// Adding custom deserializers
		gsonBuilder.registerTypeAdapter(TravelfolderUser.class, new TravelfolderUserConverter());
		gsonBuilder.registerTypeAdapter(Journey.class, new JourneyConverter());
		Gson myGson = gsonBuilder.create();
		return GsonConverterFactory.create(myGson);
	}
}