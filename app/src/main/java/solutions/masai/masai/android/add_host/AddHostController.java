package solutions.masai.masai.android.add_host;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.chat.ChatController;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.communication.HostConnection;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.model.Host;

import java.util.List;

public class AddHostController extends AppCompatActivity implements AddHostView.AddHostViewListener {

	private AddHostView view;
	private Context context;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
		handler = new Handler(Looper.getMainLooper());
		setContentView(R.layout.activity_server_list);
		view = new AddHostView(findViewById(android.R.id.content), this, this);
		view.setTitle(getString(R.string.select_host));
		List<HostConnection> hostConList = ConnectionManager.getInstance().getHostConnectionList();
		if (hostConList != null && !hostConList.isEmpty()) {
			AddHostListAdapter addHostListAdapter = new AddHostListAdapter(this, hostConList);
			view.setAddHostListAdapter(addHostListAdapter);
		} else {
			finish();
		}
	}

	@Override
	public void onHostClicked(final int position) {
		int existsAtPos = ConnectionManager.getInstance().getHostPosition(position);
		if (existsAtPos > -1 && !ConnectionManager.getInstance().getHostConnectionForPos(position).getHost().getOpenedRooms().isEmpty()) {
			startChat(existsAtPos);
		} else {
			ConnectionManager.getInstance().getHostConnectionForPos(position).initNewRoom(new HostConnection.IChannelCreatedCallback() {
				@Override
				public void onChannelCreated(String roomId) {
					startChat(position);
				}

				@Override
				public void onHostReady() {
					//not used here
				}

				@Override
				public void onConnectivityError() {
					view.showOkDialog(R.string.connection_restore);
					view.hideProgress();
				}
			});
		}
	}

	@Override
	protected void onStop() {
		handler.removeCallbacksAndMessages(null);
		super.onStop();
	}

	@Override
	public void onNavigateBack() {
		onBackPressed();
	}

	private void startChat(int pos) {
		Intent intent = new Intent(context, ChatController.class);
		intent.putExtra(C.EXTRA_HOST_CONNECTION_POSITION, pos);
		intent.putExtra(C.EXTRA_ROOM_ID, ConnectionManager.getInstance().getHostConnectionForPos(pos).getHost().getOpenedRooms().get(0)
				.getId());
		finish();
		startActivity(intent);
	}
}