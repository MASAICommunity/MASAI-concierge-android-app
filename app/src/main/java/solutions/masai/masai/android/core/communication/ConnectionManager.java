package solutions.masai.masai.android.core.communication;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import solutions.masai.masai.android.core.BackendManager;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.PrefsHelper;
import solutions.masai.masai.android.core.helper.RealmHelper;
import solutions.masai.masai.android.core.model.Host;
import solutions.masai.masai.android.core.model.User;
import solutions.masai.masai.android.core.model.realm.RocketChatUser;
import solutions.masai.masai.android.core.model.rocketchat.Room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Semko on 2016-12-02.
 */

public class ConnectionManager {

	private static ConnectionManager instance;
	private List<HostConnection> hostConnections = new ArrayList<>();
	private User user;
	private List<Host> hostList = new ArrayList<>();
	private String gcmToken;
	private static boolean shouldShowReconnectInfo = false;

	protected ConnectionManager() {
		retrieveUsername();
	}

	//region properties
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		String userJ = new Gson().toJson(user);
		for (HostConnection hostConnection : hostConnections) {
			hostConnection.setUser(user);
		}
		if (user == null) {
			PrefsHelper.getInstance().setString(C.PREFERENCES_KEY_USER, "");
		} else {
			PrefsHelper.getInstance().setString(C.PREFERENCES_KEY_USER, userJ);
		}
	}

	public String getGcmToken() {
		return gcmToken;
	}

	public void setGcmToken(String gcmToken) {
		this.gcmToken = gcmToken;
	}

	public static boolean isShouldShowInfoReconnecting() {
		return shouldShowReconnectInfo;
	}

	public static void setShouldShowInfoReconnecting(boolean shouldShowReconnectInfo) {
		ConnectionManager.shouldShowReconnectInfo = shouldShowReconnectInfo;
	}

	public List<HostConnection> getHostConnectionList() {
		return hostConnections;
	}

	public List<Host> getHostList() {
		return hostList;
	}
	//endregion

	public static ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager();
		}
		return instance;
	}

	public static void clear() {
		instance = null;
	}

	public void loadHostConnectionsFromDB() {
		for (Host host : RealmHelper.getInstance().getHosts()) {
			if (!hostList.contains(host)) {
				hostList.add(host);
			}
		}
		Collections.sort(hostList, (h1, h2) -> h1.getHumanName().compareTo(h2.getHumanName()));

		for(Host host : hostList) {
			addHostConnection(host);
		}
	}

	public List<Room> getAllClosedRooms() {
		List<Room> closedRooms = new ArrayList<>();
		for(HostConnection hostCon : hostConnections) {
			for(Room closedRoom : hostCon.getHost().getClosedRooms()) {
				closedRooms.add(closedRoom);
			}
		}
		Collections.sort(closedRooms, (r1, r2) -> r1.getClosedAt().getDate().compareTo(r2.getClosedAt().getDate()));
		return closedRooms;
	}

	public boolean areOpenRoomsAvailable() {
		boolean areOpenRoomsAvailable = false;
		for(HostConnection hostCon : hostConnections) {
			if(!hostCon.getHost().getOpenedRooms().isEmpty()) {
				areOpenRoomsAvailable = true;
				break;
			}
		}

		return areOpenRoomsAvailable;
	}

	public String getHostUrlByRoomId(String roomId) {
		for(HostConnection hostCon : hostConnections) {
			for (Room room : hostCon.getHost().getOpenedRooms()) {
				if (room.getId().equals(roomId)) {
					return hostCon.getHost().getHostRocketChatApiUrl();
				}
			}
			for (Room room : hostCon.getHost().getClosedRooms()) {
				if (room.getId().equals(roomId)) {
					return hostCon.getHost().getHostRocketChatApiUrl();
				}
			}
		}
		return null;
	}

	public int getHostPosition(int pos) {
		if (hostList.size() > pos) {
			String hostToAddUrl = hostList.get(pos).getHostUrl();
			for (HostConnection connection : hostConnections) {
				if (connection.getHost().getHostUrl().equals(hostToAddUrl)) {
					return connection.getConnectionPositionOnList();
				}
			}
		}
		return -1;
	}

	public HostConnection getHostConnectionForPos(int pos) {
		if (pos < hostConnections.size()) {
			return hostConnections.get(pos);
		}
		return null;
	}

	public void initHostListFromServer(final IHostsAddedListener listener) {
		C.L("Getting hostlist from server...");
		//demoHostList();
		BackendManager.getInstance().getBackendService().getHostList(C.AUTHORIZATION_BEARER_PREFIX + user.getIdToken())
				.enqueue(new Callback<ResponseBody>() {
					@Override
					public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
						try {
							if (response.errorBody() == null) {
								List<Host> hosts = new Gson().fromJson(response.body().string(), new TypeToken<List<Host>>() {
								}.getType());
								for (Host host : hosts) {
									if (!hostList.contains(host)) {
										RocketChatUser rcUser = RealmHelper.getInstance().getRocketChatUserForHost(host);
										if(rcUser == null) {
											rcUser = new RocketChatUser();
											rcUser.setRocketchatUrl(host.getHostRocketChatApiUrl());
										}
										host.setRcUser(rcUser);
										hostList.add(host);
									}
								}

								Collections.sort(hostList, (h1, h2) -> h1.getHumanName().compareTo(h2.getHumanName()));
								listener.onHostListUpdated();
							}
						} catch (IOException e) {
							C.L(e.getMessage());
							listener.onError();
						}
					}

					@Override
					public void onFailure(Call<ResponseBody> call, Throwable t) {
						C.L("Error getting hostlist from server!");
						listener.onError();
					}
				});
	}

	public int addConnection(Context context, final int position, boolean isConnected, final IConnectListener listener) {
		RealmHelper.getInstance().insertOrUpdateHost(hostList.get(position));
		final int connectionPos = addHostConnection(hostList.get(position));
		if (isConnected) {
			connectTo(context, position, new IConnectListener() {
				@Override
				public void onDisconnected(int pos) {
					if (connectionPos == pos) {
						listener.onDisconnected(pos);
					}
				}

				@Override
				public void onConnectedAndLoggedIn(final int pos) {
					if (connectionPos == pos) {
						listener.onConnectedAndLoggedIn(pos);
					}
				}

				@Override
				public void onBadPassword(int pos) {
					if (connectionPos == pos) {
						listener.onBadPassword(pos);
					}
				}

				@Override
				public void onUserNotFound(int pos) {
					if (connectionPos == pos) {
						listener.onUserNotFound(pos);
					}
				}

				@Override
				public void onNoUsername(int pos) {
					if (connectionPos == pos) {
						listener.onNoUsername(pos);
					}
				}

				@Override
				public void onNewMessage(int pos, Room room) {
					if (connectionPos == pos) {
						listener.onNewMessage(pos, room);
					}
				}

				@Override
				public void onConnectivityError(int pos) {
					if (connectionPos == pos) {
						listener.onConnectivityError(pos);
					}
				}

				@Override
				public void onHostNotReachable(int pos) {
					if (connectionPos == pos) {
						listener.onHostNotReachable(pos);
					}
				}

				@Override
				public void onHostLoadingFinished(int pos) {
					if (connectionPos == pos) {
						listener.onHostLoadingFinished(pos);
					}
				}

				@Override
				public void onRoomCreated(Room room) {
					if (connectionPos == room.getHostConPosition()) {
						listener.onRoomCreated(room);
					}
				}
			});
		}
		return connectionPos;
	}

	public void removeConnection(int position) {
		if (hostConnections.size() > position) {
			hostConnections.remove(position);
		}
	}

	public void removeAll() {
		while (!hostConnections.isEmpty()) {
			ConnectionManager.getInstance().getHostConnectionForPos(0).destroy();
			removeConnection(0);
		}
		hostConnections = new ArrayList<>();
	}

	public void connectToAll(Context context, IConnectListener listener) {
		for (int co0 = 0; co0 < hostConnections.size(); ++co0) {
			connectTo(context, co0, listener);
		}
	}

	public void connectTo(Context context, final int pos, final IConnectListener listener) {
		C.L("ConnectionManager connect to hostconnection " + pos);
		if (pos < hostConnections.size()) {
			HostConnection connection = hostConnections.get(pos);
			connection.connectIfDisconnectedAndSetHostListener(context, new HostConnection.IHostConnectionCallback() {
				@Override
				public void onDisconnected() {
					listener.onDisconnected(pos);
				}

				@Override
				public void onConnectedAndLoggedIn() {
					listener.onConnectedAndLoggedIn(pos);
				}

				public void onNewMessage(Room room) {
					listener.onNewMessage(pos, room);
				}

				@Override
				public void onHostReady() {
					listener.onHostLoadingFinished(pos);
				}

				@Override
				public void onHostNotReachable() {
					listener.onHostNotReachable(pos);
				}

				@Override
				public void onRoomCreated(String roomId) {
					listener.onRoomCreated(getHostConnectionForPos(pos).getHost().getRoomById(roomId));
				}

				@Override
				public void onConnectivityError() {
					listener.onConnectivityError(pos);
				}
			});
		}
	}

	public int addHostConnection(Host host) {
		C.L("ConnectionManager add hostconnection for host " + host.getHostRocketChatApiUrl());
		for (int co0 = 0; co0 < hostConnections.size(); ++co0) {
			if (hostConnections.get(co0).getHost().getHostUrl().equals(host.getHostUrl())) {
				return co0;
			}
		}
		final HostConnection connection = new HostConnection(host, user);
		connection.setConnectionPositionOnList(hostConnections.size());
		hostConnections.add(connection);
		return hostConnections.size() - 1;
	}

	public boolean isHostConnectionActive(Host host) {
		boolean isHostConnectionActive = false;
		for (HostConnection connection : ConnectionManager.getInstance().getHostConnectionList()) {
			if (connection.getHost().getHostRocketChatApiUrl().equals(host.getHostRocketChatApiUrl())) {
				isHostConnectionActive = true;
				break;
			}
		}

		return isHostConnectionActive;
	}

	private void retrieveUsername() {
		if (PrefsHelper.getInstance() != null && PrefsHelper.getInstance().contains(C.PREFERENCES_KEY_USER)) {
			user = new Gson().fromJson(PrefsHelper.getInstance().getString(C.PREFERENCES_KEY_USER), User.class);
			if (user != null) {
				C.L("user retrieved + " + user.getEmail() + ", " + user.getPassword() + ", " + user.getUsername());
			}
		}
	}

	private void demoHostList() {
		Host amazonHost = new Host();
		amazonHost.setLiveChatToken("ra69");
		amazonHost.setHostRocketChatApiUrl("http://ec2-34-253-221-13.eu-west-1.compute.amazonaws.com:3001/");
		amazonHost.setHostUrl("ws://ec2-34-253-221-13.eu-west-1.compute.amazonaws.com:3001/websocket");
		amazonHost.setHumanName("Rocketchat Amazon");
		if(!hostList.contains(amazonHost)) {
			RocketChatUser rcUser = RealmHelper.getInstance().getRocketChatUserForHost(amazonHost);
			if(rcUser == null) {
				rcUser = new RocketChatUser();
				rcUser.setRocketchatUrl(amazonHost.getHostRocketChatApiUrl());
			}
			amazonHost.setRcUser(rcUser);
			hostList.add(amazonHost);
		}
	}

	public interface IConnectListener {

		void onDisconnected(int pos);

		void onConnectedAndLoggedIn(int pos);

		void onBadPassword(int pos);

		void onUserNotFound(int pos);

		void onNoUsername(int pos);

		void onNewMessage(int pos, Room room);

		void onConnectivityError(int pos);

		void onHostNotReachable(int pos);

		void onHostLoadingFinished(int pos);

		void onRoomCreated(Room room);
	}

	public interface IHostsAddedListener {

		void onHostListUpdated();

		void onError();
	}
}