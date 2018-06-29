package solutions.masai.masai.android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import solutions.masai.masai.android.App;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.chat.ChatController;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.communication.HostConnection;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.model.Host;
import solutions.masai.masai.android.core.model.rocketchat.Room;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

	private static final String JSON_KEY = "ejson";
	private static final String MESSAGE_KEY = "message";
	private static final String TITLE_KEY = "title";
	private NotificationRocketChatModel rocketChatNotification;
	private int hostCounter = 0;

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		if (remoteMessage == null) {
			return;
		}
		C.L("Notification from " + remoteMessage.getFrom() + " has come in!");
		rocketChatNotification = new Gson().fromJson(remoteMessage.getData().get(JSON_KEY), NotificationRocketChatModel.class);
		if (App.isAppInBackground()) {
			rocketChatNotification.setMessage(remoteMessage.getData().get(MESSAGE_KEY));
			rocketChatNotification.setTitle(remoteMessage.getData().get(TITLE_KEY));
			addConnections();
		} else {
			refreshConversations();
		}
	}

	private void addConnections() {
		for (Host host : ConnectionManager.getInstance().getHostList()) {
			boolean isHostConnectionActive = ConnectionManager.getInstance().isHostConnectionActive(host);
			if (!isHostConnectionActive) {
				ConnectionManager.getInstance().addConnection(App.getContext(), ConnectionManager.getInstance().getHostList().indexOf
						(host), true, new ConnectionManager.IConnectListener() {
					@Override
					public void onDisconnected(int pos) {
						//not used here
					}

					@Override
					public void onConnectedAndLoggedIn(int pos) {
						//not used here
					}

					@Override
					public void onBadPassword(int pos) {
						//not used here
					}

					@Override
					public void onUserNotFound(int pos) {
						//not used here
					}

					@Override
					public void onNoUsername(int pos) {
						//not used here
					}

					@Override
					public void onNewMessage(int pos, Room room) {
						//not used here
					}

					@Override
					public void onConnectivityError(int pos) {
						//not used here
					}

					@Override
					public void onHostNotReachable(int pos) {
						hostCounter++;
						if(hostCounter == ConnectionManager.getInstance().getHostList().size()) {
							initMessage();
						}
					}

					@Override
					public void onHostLoadingFinished(int pos) {
						hostCounter++;
						if(hostCounter == ConnectionManager.getInstance().getHostList().size()) {
							initMessage();
						}
					}

					@Override
					public void onRoomCreated(Room room) {
						//not used here
					}
				});
			}
		}
	}

	private void initMessage() {
		Room room = null;
		Integer hostConPosition = null;
		for (HostConnection hostCon : ConnectionManager.getInstance().getHostConnectionList()) {
			if(room == null) {
				room = hostCon.getHost().getRoomById(rocketChatNotification.getRoomId());
			}
			if (rocketChatNotification.getHostUrl().contains(hostCon.getHost().getHostRocketChatApiUrl())) {
				hostConPosition = hostCon.getConnectionPositionOnList();
			}
		}

		if (hostConPosition != null) {
			rocketChatNotification.setHostConPosition(hostConPosition);
			if (room == null) {
				createNewRoom();
			} else {
				createNotification();
			}
		}
	}

	private void createNewRoom() {
		ConnectionManager.getInstance().getHostConnectionForPos(rocketChatNotification.getHostConPosition()).initNewRoom(
				new HostConnection.IChannelCreatedCallback() {
			@Override
			public void onChannelCreated(String rid) {
				rocketChatNotification.setRoomId(rid);
				createNotification();
			}

			@Override
			public void onHostReady() {
				//not used here
			}

			@Override
			public void onConnectivityError() {
				//not used here
			}
		});
	}

	private void createNotification() {
		Intent intent = new Intent(this, ChatController.class);
		intent.putExtra(C.EXTRA_HOST_CONNECTION_POSITION, rocketChatNotification.getHostConPosition());
		intent.putExtra(C.EXTRA_ROOM_ID, rocketChatNotification.getRoomId());
		PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.mipmap.ic_launcher)
						.setContentTitle(rocketChatNotification.getTitle())
						.setContentIntent(pendingIntent)
						.setContentText(rocketChatNotification.getMessage());
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = mBuilder.build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		mNotificationManager.notify(001, notification);
	}

	private void refreshConversations() {
		Room room = null;
		for(HostConnection hostCon : ConnectionManager.getInstance().getHostConnectionList()) {
			room = hostCon.getHost().getRoomById(rocketChatNotification.getRoomId());
			if(room != null) {
				break;
			}
		}
		if(room == null) {
			Intent intent = new Intent(C.REFRESH_CONVERSATIONS_BY_NOTIFICATION);
			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		}
	}

	class NotificationRocketChatModel {

		@SerializedName("rid")
		private String roomId;
		@SerializedName("host")
		private String hostUrl;
		private String message;
		private int hostConPosition;
		private String title;

		//region properties
		public String getRoomId() {
			return roomId;
		}

		public void setRoomId(String roomId) {
			this.roomId = roomId;
		}

		public String getHostUrl() {
			return hostUrl;
		}

		public void setHostUrl(String hostUrl) {
			this.hostUrl = hostUrl;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public int getHostConPosition() {
			return hostConPosition;
		}

		public void setHostConPosition(int hostConPosition) {
			this.hostConPosition = hostConPosition;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
		//endregion
	}
}