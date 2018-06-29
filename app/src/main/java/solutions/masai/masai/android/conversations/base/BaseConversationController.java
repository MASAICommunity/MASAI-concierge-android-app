package solutions.masai.masai.android.conversations.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import solutions.masai.masai.android.chat.ChatController;
import solutions.masai.masai.android.core.BackendManager;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.communication.HostConnection;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.model.User;
import solutions.masai.masai.android.core.model.rocketchat.Room;

/**
 * Created by cWahl on 15.11.2017.
 */

public abstract class BaseConversationController extends AppCompatActivity {

	protected Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler(Looper.getMainLooper());
	}

	public void startChat(int position, Room room, boolean showPermissionRequest, String... searchTerm) {
		final User user = ConnectionManager.getInstance().getUser();
		BackendManager.getInstance().getBackendService().createTransaction(C.AUTHORIZATION_BEARER_PREFIX + user.getIdToken())
				.enqueue(new Callback<ResponseBody>() {
					@Override
					public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
						//not used here
					}

					@Override
					public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
						//not used here
					}
				});
		if (getIntent().hasExtra(C.EXTRA_ROOM_ID)) {
			List<HostConnection> hostConnectionList = ConnectionManager.getInstance().getHostConnectionList();
			for (int co0 = 0; co0 < hostConnectionList.size(); ++co0) {
				if (hostConnectionList.get(co0).getHost().getOpenedRooms().contains(room)) {
					position = co0;
				}
			}
		}

		Intent openConversation = new Intent(this, ChatController.class);
		openConversation.putExtra(C.EXTRA_HOST_CONNECTION_POSITION, position);
		openConversation.putExtra(C.EXTRA_ROOM_ID, room.getId());
		openConversation.putExtra(C.EXTRA_SHOW_PERMISSION_REQUEST, showPermissionRequest);
		if(searchTerm.length == 1 && searchTerm[0] != null && !searchTerm[0].isEmpty()) {
			openConversation.putExtra(C.EXTRA_SEARCH_TERM, searchTerm[0]);
		}
		startActivity(openConversation);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return false;
	}
}
