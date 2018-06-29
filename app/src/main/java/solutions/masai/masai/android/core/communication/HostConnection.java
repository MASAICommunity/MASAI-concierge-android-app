package solutions.masai.masai.android.core.communication;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.firebase.tubesock.WebSocketException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import bolts.Task;
import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.SubscribeListener;
import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import solutions.masai.masai.android.App;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.chat.ChatUtilities;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.FileHelper;
import solutions.masai.masai.android.core.helper.OkHttpHelper;
import solutions.masai.masai.android.core.helper.RealmHelper;
import solutions.masai.masai.android.core.helper.RegexHelper;
import solutions.masai.masai.android.core.model.Host;
import solutions.masai.masai.android.core.model.LiveChatMessage;
import solutions.masai.masai.android.core.model.LiveChatRegisterGuest;
import solutions.masai.masai.android.core.model.LiveChatResume;
import solutions.masai.masai.android.core.model.LivechatUser;
import solutions.masai.masai.android.core.model.Message;
import solutions.masai.masai.android.core.model.MessageAttachment;
import solutions.masai.masai.android.core.model.MeteorFile;
import solutions.masai.masai.android.core.model.MeteorFileSendMessage;
import solutions.masai.masai.android.core.model.MeteorMessage;
import solutions.masai.masai.android.core.model.MeteorRoomId;
import solutions.masai.masai.android.core.model.SyncState;
import solutions.masai.masai.android.core.model.Timestamp;
import solutions.masai.masai.android.core.model.User;
import solutions.masai.masai.android.core.model.rocketchat.ClosedAt;
import solutions.masai.masai.android.core.model.rocketchat.PushUpdate;
import solutions.masai.masai.android.core.model.rocketchat.Room;

/**
 * Created by Semko on 2017-02-17.
 */

public class HostConnection implements MeteorCallback {

	private static final String ROCKETCHAT_MSG_KEY = "msg";
	private Meteor meteor;
	private User user;
	private Host host;
	private IHostConnectionCallback hostConnectionListener;
	private int connectionPositionOnList;
	private String subscriptionId;
	private Handler handler = new Handler(Looper.getMainLooper());

	public HostConnection(Host host, User user) {
		this.user = user;
		this.host = host;
	}

	//region properties
	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Host getHost() {
		return host;
	}

	public int getConnectionPositionOnList() {
		return connectionPositionOnList;
	}

	public void setConnectionPositionOnList(int connectionPositionOnList) {
		this.connectionPositionOnList = connectionPositionOnList;
	}

	public boolean isConnected() {
		return meteor != null ? meteor.isConnected() : false;
	}
	//endregion

	@Override
	public void onConnect(boolean signedInAutomatically) {
		C.L("Host " + host.getHostRocketChatApiUrl() + " onConnect");

		try {
			if(host.getRcUser().getPushId() == null) {
				host.getRcUser().setPushId(UUID.randomUUID().toString());
			}
			PushUpdate pushUpdate = new PushUpdate();
			pushUpdate.setId(host.getRcUser().getPushId());
			pushUpdate.setAppName(C.GOOGLE_APP_NAME);
			pushUpdate.setUserId(host.getRcUser() != null && host.getRcUser().getUserId() != null ? host.getRcUser().getUserId() : null);

			meteor.call("raix:push-update", new Object[] { pushUpdate }, new ResultListener() {
				@Override
				public void onSuccess(String result) {
					C.L("GCM-Token sent to host: " + host.getHostRocketChatApiUrl());
				}

				@Override
				public void onError(String error, String reason, String details) {
					C.L("GCM-Token not sent to host: " + host.getHostRocketChatApiUrl() + "! Reason: " + reason);
				}
			});
		} catch (Exception ex) {
			C.L("Error sending gcm token to host: " + host.getHostRocketChatApiUrl() + "\nError: " + ex.getMessage());
		}

		initLiveChatSequence();
	}

	@Override
	public void onDisconnect() {
		C.L("Host " + host.getHostRocketChatApiUrl() + " onDisconnect");
		if (hostConnectionListener != null) {
			hostConnectionListener.onDisconnected();
		}
		subscriptionId = null;
	}

	@Override
	public void onException(Exception e) {
		C.L("Host " + host.getHostRocketChatApiUrl() + " onException: " + e.getMessage());
		/*if(e instanceof WebSocketException) {
			if(!e.getMessage().contains("IO Error") && !e.getMessage().contains("IO Exception")) {
				if (hostConnectionListener != null) {
					meteor.disconnect();
					RealmHelper.getInstance().removeHost(host.getHostRocketChatApiUrl());
					hostConnectionListener.onHostNotReachable();
				}
				subscriptionId = null;
			}
		}*/
	}

	@Override
	public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
		l("onDataAdded collectionName = " + collectionName + ", documentID = " + documentID + ", newValuesJson = " + newValuesJson);
	}

	@Override
	public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
		C.L("Host " + host.getHostRocketChatApiUrl() + " onDataChanged");
		l("onDataAdded collectionName = " + collectionName + ", documentID = " + documentID + ", updatedValuesJson = " + updatedValuesJson
				+ ", removedValuesJson = " + removedValuesJson);
		String moddedJsonString = RegexHelper.simplifyDate(updatedValuesJson);
		moddedJsonString = RegexHelper.simplifyLocation(moddedJsonString);
		try {
			JSONObject jsonObject = new JSONObject(moddedJsonString);
			if (jsonObject.has(C.JSON_FIELD_ARGS)) {
				JSONArray args = jsonObject.getJSONArray(C.JSON_FIELD_ARGS);
				if (args.length() > 0) {
					JSONObject object = args.getJSONObject(0);
					String roomId = object.get("rid").toString();
					Room room = host.getRoomById(roomId);
					if (object.get(ROCKETCHAT_MSG_KEY).toString().contains(C.ROCKETCHAT_ROOM_CLOSED_KEY)) {
						object.remove(ROCKETCHAT_MSG_KEY);
						object.put(ROCKETCHAT_MSG_KEY, App.getContext().getString(R.string.chat_closed));
						ClosedAt closedAt = new ClosedAt();
						closedAt.setDate(String.valueOf(System.currentTimeMillis()));
						host.getRoomById(roomId).setClosedAt(closedAt);
						host.getOpenedRooms().remove(room);
						host.getClosedRooms().add(room);
						C.L("Room " + room.getId() + " is closed.");
					}
					RealmHelper.getInstance().addMessageFromJson(object);
					hostConnectionListener.onNewMessage(room);
				}
			}
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	@Override
	public void onDataRemoved(String collectionName, String documentID) {
		l("onDataRemoved collectionName = " + collectionName + ", documentID = " + documentID);
	}

	public void destroy() {
		if(meteor != null) {
			meteor.disconnect();
			meteor.removeCallback(this);
		}
	}

	public void initNewRoom(IChannelCreatedCallback listener) {
		Room room = new Room();
		room.setId(UUID.randomUUID().toString());
		room.setMessagesCounter(0);
		room.setHostHumanName(host.getHumanName());
		room.setHostConPosition(connectionPositionOnList);
		host.getOpenedRooms().add(room);
		subscriptionId = null;
		listener.onChannelCreated(room.getId());
	}

	public void subscribeLoginServiceConfiguration(SubscribeListener subscribeListener) {
		meteor.subscribe("meteor.loginServiceConfiguration", new Object[]{null}, subscribeListener);
	}

	public void connectIfDisconnectedAndSetHostListener(Context context, HostConnection.IHostConnectionCallback hostConnectionListener) {
		C.L("connectIfDisconnectedAndSetHostListener host " + host.getHostRocketChatApiUrl());
		if (hostConnectionListener != null) {
			this.hostConnectionListener = hostConnectionListener;
		}
		if (meteor == null || !meteor.isConnected()) {
			meteor = new Meteor(context, host.getHostUrl());
			meteor.addCallback(this);
			meteor.connect();
		} else if (meteor.isConnected() && hostConnectionListener != null) {
			hostConnectionListener.onConnectedAndLoggedIn();
		}
	}

	public void liveChatGetInitialData(final IChannelCreatedCallback listener) {
		subscribeLoginServiceConfiguration(new SubscribeListener() {
			@Override
			public void onSuccess() {
				l("meteor.loginServiceConfiguration");
				registerOrLogin(listener);
			}

			@Override
			public void onError(String error, String reason, String details) {
				l("meteor.loginServiceConfiguration ERRORRRR = " + error + ", reason = " + reason + ", details = " + details);
				listener.onConnectivityError();
			}
		});
	}

	public void liveChatInitRoomsForHost(String liveChatToken, IChannelCreatedCallback listener) {
		meteor.call("masai:getRooms", new Object[]{liveChatToken}, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				if (!result.equals("[]")) {
					List<Room> rooms = new Gson().fromJson(result, new TypeToken<ArrayList<Room>>() {
					}.getType());
					if (rooms != null) {
						host.getOpenedRooms().clear();
						host.getClosedRooms().clear();
						for (Room r : rooms) {
							r.setHostHumanName(host.getHumanName());
							r.setHostConPosition(connectionPositionOnList);
							if (r.getClosedAt() == null) {
								host.getOpenedRooms().add(r);
								subscribeToRoom(r.getId());
								listener.onChannelCreated(r.getId());
							} else {
								host.getClosedRooms().add(r);
							}
						}
						RealmHelper.getInstance().insertOrUpdateHost(host);
					}
				}
				listener.onHostReady();
			}

			@Override
			public void onError(String error, String reason, String details) {
				C.L("masai:getRooms for host " + host.getHostRocketChatApiUrl() + " ERRORRRR = " + error + ", reason = " +
						reason + ", details = " + details);
				listener.onConnectivityError();
			}
		});
	}

	public void loadMessagesFromWhileIWasAway(final String roomId, final IGetMessagesCallback listener) {
		C.L("loadMessagesFromWhileIWasAway " + roomId);
		Message message = RealmHelper.getInstance().getFirstMessage(roomId);
		if (message == null) {
			long ts = System.currentTimeMillis();
			loadHistory(roomId, 0, 50, ts, listener);
		} else {
			loadHistory(roomId, 0, 50, 0, listener);
		}
	}

	public void subscribeToRoom(final String roomId) {
		if (subscriptionId == null) {
			subscriptionId = meteor.subscribe("stream-room-messages", new Object[]{roomId, false}, new SubscribeListener() {
				@Override
				public void onSuccess() {
					l("Subscribed ");
				}

				@Override
				public void onError(String error, String reason, String details) {
					l("Subscribe ERROR + " + error + ", reason = " + reason + ", details = " + details);
				}
			});
		}
	}

	public void sendMessage(String messageId, String roomId, final String message, final float lat, final float lng
			, final ISendMessagesCallback listener) {
		C.L("trying to send message");
		Room room = host.getRoomById(roomId);
		if (room.getMessagesCounter() == 0) {
			sendCreateRoomMessage(room, message, new IChannelCreatedCallback() {
				@Override
				public void onChannelCreated(String roomId) {
					if (hostConnectionListener != null) {
						hostConnectionListener.onRoomCreated(roomId);
					}
				}

				@Override
				public void onHostReady() {
					//not used here
				}

				@Override
				public void onConnectivityError() {
					if (hostConnectionListener != null) {
						hostConnectionListener.onConnectivityError();
					}
				}
			});
		} else {
			try {
				if (!host.isValid()) {
					listener.onConnectivityError();
					return;
				}
				if (messageId == null) {
					messageId = UUID.randomUUID().toString();
				}

				final String messageID = messageId;
				final JSONObject messageJsonObject = getMessageJsonObject(messageID, room, message);
				final MeteorMessage messageMeteor = new MeteorMessage();
				messageMeteor.setMsg(message);
				messageMeteor.setRid(roomId);
				messageMeteor.setId(messageId);
				if (lat != 0 && lng != 0) {
					messageMeteor.setPoint(lat, lng);
					messageJsonObject.put(C.JSON_FIELD_LOCATION, new JSONObject()
							.put(C.JSON_FIELD_LOCATION_TYPE, C.JSON_VALUE_LOCATION_POINT)
							.put(C.JSON_FIELD_LOCATION_LAT, lat)
							.put(C.JSON_FIELD_LOCATION_LNG, lng));
				}
				RealmHelper.getInstance().addMessageFromJson(messageJsonObject);
				listener.onMessageStateUpdated();
				if (meteor.isConnected()) {
					meteor.call(C.RPC_SEND_MESSAGE, new Object[]{messageMeteor}, new ResultListener() {
						@Override
						public void onSuccess(String result) {
							C.L("message Sent returned" + result);
							handler.post(new Runnable() {
								@Override
								public void run() {
									RealmHelper.getInstance().updateMessageState(roomId, messageID, SyncState.SYNCED);
									listener.onMessageStateUpdated();
								}
							});
						}

						@Override
						public void onError(String error, String reason, String details) {
							C.L("message Sent ERROR returned" + error + ", reason = " + reason + "details = " + details);
							handler.post(new Runnable() {
								@Override
								public void run() {
									RealmHelper.getInstance().updateMessageState(roomId, messageID, SyncState.FAILED);
									listener.onConnectivityError();
								}
							});
						}
					});
				} else {
					handler.post(new Runnable() {
						@Override
						public void run() {
							RealmHelper.getInstance().updateMessageState(roomId, messageID, SyncState.FAILED);
							listener.onConnectivityError();
						}
					});
				}
			} catch (JSONException je) {
				je.printStackTrace();
				listener.onConnectivityError();
			}
		}
	}

	public void sendFileMessage(final String storageType, final String roomId, final String downloadUrl, final long fileSize, final String
			mimeType, final String fileName, final String filePath, final ISendFileCallback listener) {
		C.L("trying to send file " + fileName);
		Task.callInBackground(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				C.L("FILEUPLOAD #5.0");
				String id = Uri.parse(downloadUrl).getLastPathSegment();
				C.L("ID=" + id);
				MeteorFileSendMessage fileSendMessageMeteor = new MeteorFileSendMessage();
				fileSendMessageMeteor.setSize(fileSize);
				fileSendMessageMeteor.setId(id);
				fileSendMessageMeteor.setType(mimeType);
				fileSendMessageMeteor.setUrl(downloadUrl);
				fileSendMessageMeteor.setName(fileName);
				meteor.call(C.RPC_SEND_FILE_MESSAGE, new Object[]{roomId, storageType, fileSendMessageMeteor}, new ResultListener() {
					@Override
					public void onSuccess(String result) {
						listener.onFileSent(fileName, filePath);
						l("ROCKETCHATUPLOAD SUCCESS = " + result);
					}

					@Override
					public void onError(String error, String reason, String details) {
						l("ROCKETCHATUPLOAD SEND FILE MESSAGE ERRORRRR = " + error + ", reason = " + reason + ", details = " + details);
						if (reason.contains("File exceeds allowed size")) {
							listener.onFileTooBigError(filePath);
						} else {
							listener.onConnectionError(filePath);
						}
					}
				});
				return null;
			}
		});
	}

	public void requestFileSpaceAndUpload(String roomId, final String filePath, final ISendFileCallback listener) {
		C.L("trying to send file");
		try {
			if (!host.isValid()) {
				listener.onConnectionError(filePath);
				return;
			}
			final JSONObject messageJsonObject = new JSONObject()
					.put(C.JSON_FIELD_ID, filePath)
					.put(C.JSON_FIELD_ROOM_ID, roomId)
					.put(C.JSON_FIELD_MESSAGE, "")
					.put(C.JSON_FIELD_POS_ON_THE_LIST, connectionPositionOnList)
					.put(C.JSON_FIELD_SYNCSTATE, SyncState.SYNCING)
					.put(C.JSON_FIELD_USER_SHORT, new JSONObject()
							.put(C.JSON_FIELD_USERNAME, user.getUsername())
							.put(C.JSON_FIELD_USER_ID, host.getRcUser().getUserId()))
					.put(C.JSON_FIELD_ATTACHMENTS, new JSONArray().put(new JSONObject().put(C.JSON_FIELD_TITLE, C.TITLE_VALUE_FILE).put(C
							.JSON_FIELD_TITLE_URL, filePath)))
					.put(C.JSON_FIELD_TIMESTAMP, System.currentTimeMillis());
			RealmHelper.getInstance().addMessageFromJson(messageJsonObject);
			listener.onImageAddedToDatabase();
			C.L("FILEUPLOAD #1");
			final String roomId2 = roomId;
			Task.callInBackground(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					C.L("FILEUPLOAD #2.0 filepath = " + filePath);
					String[] pathSegments = filePath.split("/");
					final String fileName = pathSegments[pathSegments.length - 1];
					final String fileMimeType = ChatUtilities.getMimeType(filePath);
					final File file = new File(filePath);
					final long fileSize = file.length();
					MeteorFile fileMeteor = new MeteorFile();
					fileMeteor.setName(fileName);
					fileMeteor.setSize(fileSize);
					fileMeteor.setType(fileMimeType);
					MeteorRoomId roomIdMeteor = new MeteorRoomId();
					roomIdMeteor.setRid(roomId2);
					if (meteor.isConnected()) {
						meteor.call(C.RPC_UPLOAD_REQUEST, new Object[]{"rocketchat-uploads", fileMeteor, roomIdMeteor}, new ResultListener
								() {
							@Override
							public void onSuccess(String result) {
								l("ROCKETCHATUPLOAD success = " + result);
								parseResultAndUpload(file, roomId2, result, fileName, filePath, fileSize, fileMimeType, listener);
							}

							@Override
							public void onError(String error, String reason, String details) {
								l("ROCKETCHATUPLOAD ERRORRRR = " + error + ", reason = " + reason + ", details = " + details);
								if (error != null && error.contains("File exceeds allowed size")) {
									listener.onFileTooBigError(filePath);
								} else if (error != null && error.contains("Invalid file type")) {
									listener.onInvalidFileType(filePath);
								} else {
									listener.onServerError(filePath);
								}
							}
						});
					} else {
						listener.onConnectionError(filePath);
					}
					return null;
				}
			});
		} catch (JSONException e) {
			C.L("FILEUPLOAD #1.1");
			e.printStackTrace();
			listener.onConnectionError(filePath);
		}
	}

	public void downloadOrOpenFileFromAttachment(final Context context, MessageAttachment attachment, final String token, final String
			userId) {
		String fileLink = attachment.getTitleLink();
		if (fileLink.startsWith("/")) {
			fileLink = fileLink.substring(1, fileLink.length());
		}
		final String url = ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost()
				.getHostRocketChatApiUrl().concat(fileLink);
		downloadOrOpenFileFormUrl(context, url, userId, token);
	}

	public void downloadOrOpenFileFormUrl(final Context context, final String url, final String userId, final String token) {
		l("FILE URL = " + url);
		handler.post(new Runnable() {
			@Override
			public void run() {
				FileHelper.downloadOrOpenFileFromUrl(context, url, userId, token);
			}
		});
	}

	public void resendMessage(String filePath, Message message, final ISendFileCallback fileListener, final ISendMessagesCallback
			listener) {
		if (filePath == null) {
			float lat = 0;
			float lng = 0;
			if (message.getLocation() != null) {
				lat = message.getLocation().getLat();
				lng = message.getLocation().getLng();
			}
			sendMessage(message.getId(), message.getRoomId(), message.getMessage(), lat, lng, listener);
		} else {
			requestFileSpaceAndUpload(message.getRoomId(), filePath, fileListener);
		}
	}

	public void resendUnsynchronizedMessages(String roomId, ISendMessagesCallback messaageListener, ISendFileCallback fileListener) {
		List<Message> messageList = RealmHelper.getInstance().getUnsentMessages(roomId);
		for (Message message : messageList) {
			String filePath = null;
			if (message.getAttachments() != null) {
				for (MessageAttachment attachment : message.getAttachments()) {
					if (attachment.getTitle().contains(C.TITLE_VALUE_FILE) && !attachment.getTitleUrl().isEmpty()) {
						filePath = attachment.getTitleUrl();
					}
				}
			}
			resendMessage(filePath, message, fileListener, messaageListener);
		}
	}

	private void initLiveChatSequence() {
		liveChatGetInitialData(new IChannelCreatedCallback() {
			@Override
			public void onChannelCreated(String roomId) {
				if (hostConnectionListener != null) {
					hostConnectionListener.onRoomCreated(roomId);
				}
			}

			@Override
			public void onHostReady() {
				if (hostConnectionListener != null) {
					hostConnectionListener.onHostReady();
				}
			}

			@Override
			public void onConnectivityError() {
				if (hostConnectionListener != null) {
					hostConnectionListener.onConnectivityError();
				}
			}
		});
	}

	private void registerOrLogin(final IChannelCreatedCallback listener) {
		handler.post(() -> {
			if (host.isValid()) {
				RealmHelper.getInstance().executeCustomTransaction(realm -> {
					long currentTimestamp = System.currentTimeMillis();
					long tokenExpires = currentTimestamp + 1;
					if(host.getRcUser().getLoginTokenExpires() != null) {
						tokenExpires = Long.valueOf(host.getRcUser().getLoginTokenExpires()).longValue();
					}
					if (host.getRcUser().getLoginToken() == null || host.getRcUser().getUserId() == null ||
							currentTimestamp >= tokenExpires) {
						liveChatRegisterGuest(host.getLiveChatToken(), user.getUsername(), user.getEmail(), listener);
					} else {
						liveChatLoginGuest(host.getRcUser().getLoginToken(), host.getRcUser().getUserId(), listener);
					}
				});
			} else {
				listener.onConnectivityError();
			}
		});
	}

	private void liveChatLoginGuest(final String newLiveChatUserLoginToken, final String newLiveChatUserId, final IChannelCreatedCallback
			listener) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				RealmHelper.getInstance().executeCustomTransaction(new Realm.Transaction() {
					@Override
					public void execute(Realm realm) {
						C.L("Login " + host.getHostRocketChatApiUrl());
						if (!host.isValid()) {
							listener.onConnectivityError();
							return;
						}
						host.getRcUser().setUserId(newLiveChatUserId);
						host.getRcUser().setRocketchatUrl(host.getHostRocketChatApiUrl());
						host.getRcUser().setLoginToken(newLiveChatUserLoginToken);
						realm.copyToRealmOrUpdate(host.getRcUser());
						realm.copyToRealmOrUpdate(host);
						LiveChatResume liveChatResume = new LiveChatResume();
						liveChatResume.setResume(newLiveChatUserLoginToken);
						meteor.call("login", new Object[]{liveChatResume}, new ResultListener() {
							@Override
							public void onSuccess(String result) {
								C.L("login success = " + result);
								String tokenExpires = new Gson().fromJson(result, JsonElement.class).getAsJsonObject().get("tokenExpires").
										getAsJsonObject().get("$date").toString();
								host.getRcUser().setLoginTokenExpires(tokenExpires);
								RealmHelper.getInstance().executeCustomTransaction(realm1 -> {
									realm1.copyToRealmOrUpdate(host.getRcUser());
									realm1.copyToRealmOrUpdate(host);
								});
								hostConnectionListener.onConnectedAndLoggedIn();
								liveChatInitRoomsForHost(host.getLiveChatToken(), listener);
							}

							@Override
							public void onError(String error, String reason, String details) {
								C.L("login ERRORRRR = " + error + ", reason = " + reason + ", details = " + details);
								listener.onConnectivityError();
							}
						});

						meteor.call("raix:push-setuser", new Object[]{ host.getRcUser().getPushId() }, new ResultListener() {
							@Override
							public void onSuccess(String result) {
								C.L("GCM Token success");
							}

							@Override
							public void onError(String error, String reason, String details) {
								C.L("GCM Token error");
							}
						});
					}
				});
			}
		});
	}

	private void liveChatRegisterGuest(String token, String userName, String email, final IChannelCreatedCallback listener) {
		C.L("Register " + host.getHostRocketChatApiUrl());
		LivechatUser liveChatUser = new LivechatUser();
		liveChatUser.setEmail(email);
		liveChatUser.setName(userName);
		liveChatUser.setToken(token);
		meteor.call("livechat:registerGuest", new Object[]{liveChatUser}, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				l("livechat:registerGuest success = " + result);
				LiveChatRegisterGuest liveChatRegisterGuest = new Gson().fromJson(result, LiveChatRegisterGuest.class);
				liveChatLoginGuest(liveChatRegisterGuest.getToken(), liveChatRegisterGuest.getUserId(), listener);
			}

			@Override
			public void onError(String error, String reason, String details) {
				l("livechat:registerGuest ERRORRRR = " + error + ", reason = " + reason + ", details = " + details);
				listener.onConnectivityError();
			}
		});
	}

	private void sendCreateRoomMessage(Room room, String message, final IChannelCreatedCallback listener) {
		subscriptionId = null;
		LiveChatMessage liveChatMessage = new LiveChatMessage();
		liveChatMessage.setToken(this.host.getLiveChatToken());
		liveChatMessage.setRid(room.getId());
		liveChatMessage.setMsg(message);
		liveChatMessage.setId(UUID.randomUUID().toString());

		room.setMessagesCounter(room.getMessagesCounter() + 1);
		meteor.call("sendMessageLivechat", new Object[]{liveChatMessage}, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				l("sendMessageLivechat success = " + result);
				LiveChatMessage lcMessage = new Gson().fromJson(result, LiveChatMessage.class);
				room.setId(lcMessage.getRid());
				subscribeToRoom(room.getId());

				final JSONObject messageJsonObject = getMessageJsonObject(lcMessage.get_id(), room, message);
				if(messageJsonObject != null) {
					RealmHelper.getInstance().addMessageFromJson(messageJsonObject);
				} else {
					listener.onConnectivityError();
				}

				listener.onChannelCreated(room.getId());
			}

			@Override
			public void onError(String error, String reason, String details) {
				l("sendMessageLivechat ERRORRRR = " + error + ", reason = " + reason + ", details = " + details);
				listener.onConnectivityError();
			}
		});
	}

	private void loadHistory(final String roomId, final long timestamp, final int count, final long lastSeen, final IGetMessagesCallback
			listener) {
		C.L("SAFE loadHistory");
		Timestamp timestampTS = new Timestamp();
		timestampTS.setTimestamp(timestamp);
		Timestamp lastSeenTS = new Timestamp();
		lastSeenTS.setTimestamp(lastSeen);
		Object oldestMessageTimestamp = timestamp > 0 ? timestampTS : null;
		Object lastMessageTimestamp = lastSeen > 0 ? timestampTS : null;
		Object args[] = new Object[]{roomId, oldestMessageTimestamp, count, lastMessageTimestamp};
		meteor.call(C.RPC_LOAD_HISTORY, args, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				try {
					C.L("messages downloaded = " + result);
					String modedJsonString = RegexHelper.simplifyDate(result);
					modedJsonString = RegexHelper.simplifyLocation(modedJsonString);
					C.L("messages downloaded and moded = " + modedJsonString);
					JSONObject messagesJsonObject = new JSONObject(modedJsonString);
					if (messagesJsonObject.has(C.JSON_FIELD_MESSAGES)) {
						final JSONArray messageJsonArray = messagesJsonObject.getJSONArray(C.JSON_FIELD_MESSAGES);
						for (int co0 = 0; co0 < messageJsonArray.length(); ++co0) {
							if (messageJsonArray.get(co0).toString().contains(C.ROCKETCHAT_ROOM_CLOSED_KEY)) {
								messageJsonArray.getJSONObject(co0).remove(ROCKETCHAT_MSG_KEY);
								messageJsonArray.getJSONObject(co0).put(ROCKETCHAT_MSG_KEY, App.getContext().getString(R.string
										.chat_closed));
								C.L("Room " + messageJsonArray.getJSONObject(co0).get("rid") + " is closed.");
							}
							messageJsonArray.getJSONObject(co0).put(C.JSON_FIELD_POS_ON_THE_LIST, connectionPositionOnList);
						}
						handler.post(new Runnable() {
							@Override
							public void run() {
								RealmHelper.getInstance().addMessagesFromJson(messageJsonArray, connectionPositionOnList);
								listener.onMessagesReady();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
					listener.onConnectivityError();
				}
			}

			@Override
			public void onError(String error, String reason, String details) {
				listener.onConnectivityError();
			}
		});
	}

	private void parseResultAndUpload(final File file, final String roomId, String result, final String filename, final String filePath,
	                                  final long filesize, final String mimeType, final ISendFileCallback listener) {
		try {
			C.L("FILEUPLOAD #4.0");
			JSONObject info = new JSONObject(result);
			String uploadUrl = info.getString("upload");
			final String downloadUrl = info.getString("download");
			JSONArray postDataList = info.getJSONArray("postData");
			MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
			for (int i = 0; i < postDataList.length(); i++) {
				JSONObject postData = postDataList.getJSONObject(i);
				bodyBuilder.addFormDataPart(postData.getString("name"), postData.getString("value"));
			}
			C.L("FILEUPLOAD #4.1");
			bodyBuilder.addFormDataPart("file", filename,
					new RequestBody() {
						private long numBytes = 0;

						@Override
						public MediaType contentType() {
							return MediaType.parse(mimeType);
						}

						@Override
						public long contentLength() throws IOException {
							return filesize;
						}

						@Override
						public void writeTo(BufferedSink sink) throws IOException {
							try {
								C.L("FILEUPLOAD #4.2");
								try (Source source = Okio.source(file)) {
									long readBytes;
									while ((readBytes = source.read(sink.buffer(), 8192)) > 0) {
										numBytes += readBytes;
									}
									sendFileMessage("s3", roomId, downloadUrl, filesize, mimeType, filename, filePath, listener);
								}
							} catch (FileNotFoundException e) {
								e.printStackTrace();
								listener.onInvalidFileType(filePath);
							} catch (Exception e) {
								e.printStackTrace();
								listener.onConnectionError(filePath);
							}
						}
					});
			C.L("FILEUPLOAD #4.3");
			final Request request = new Request.Builder()
					.url(uploadUrl)
					.post(bodyBuilder.build())
					.build();
			Task.callInBackground(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					Response response = OkHttpHelper.getClientForUploadFile().newCall(request).execute();
					return null;
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
			C.L("FILEUPLOAD #4.4");
			listener.onConnectionError(filePath);
		}
	}

	private JSONObject getMessageJsonObject(String messageId, Room room, String message) {
		JSONObject messageJsonObject = null;
		try {
			messageJsonObject = new JSONObject()
					.put(C.JSON_FIELD_ID, messageId)
					.put(C.JSON_FIELD_ROOM_ID, room.getId())
					.put(C.JSON_FIELD_MESSAGE, message)
					.put(C.JSON_FIELD_POS_ON_THE_LIST, connectionPositionOnList)
					.put(C.JSON_FIELD_SYNCSTATE, SyncState.SYNCING)
					.put(C.JSON_FIELD_USER_SHORT, new JSONObject()
							.put(C.JSON_FIELD_USERNAME, user.getUsername())
							.put(C.JSON_FIELD_USER_ID, host.getRcUser().getUserId()))
					.put(C.JSON_FIELD_TIMESTAMP, System.currentTimeMillis());
		} catch(JSONException ex) {
			ex.printStackTrace();
			messageJsonObject = null;
		}

		return messageJsonObject;
	}

	private void l(String l) {
		Log.e("DDP_ROCKET_CHAT = ", l);
	}

	public interface ISendFileCallback {

		void onFileSent(String name, String filePath);

		void onImageAddedToDatabase();

		void onFileTooBigError(String filePath);

		void onConnectionError(String filePath);

		void onInvalidFileType(String filePath);

		void onServerError(String filePath);
	}

	public interface IBaseErrorCallback {

		void onConnectivityError();
	}

	public interface IChannelCreatedCallback extends IBaseErrorCallback {

		void onChannelCreated(String roomId);

		void onHostReady();
	}

	public interface IGetMessagesCallback extends IBaseErrorCallback {

		void onMessagesReady();
	}

	public interface ISendMessagesCallback extends IBaseErrorCallback {

		void onMessageStateUpdated();
	}

	public interface IResetPasswordCallback extends IBaseErrorCallback {

		void onPasswordReset();
	}

	public interface IHostConnectionCallback extends IBaseErrorCallback {

		void onDisconnected();

		void onConnectedAndLoggedIn();

		void onNewMessage(Room room);

		void onHostReady();

		void onHostNotReachable();

		void onRoomCreated(String roomId);
	}
}