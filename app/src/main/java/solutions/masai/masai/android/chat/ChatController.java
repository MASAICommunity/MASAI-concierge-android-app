package solutions.masai.masai.android.chat;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import com.google.android.gms.maps.model.LatLng;

import solutions.masai.masai.android.App;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.communication.HostConnection;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.ConnectivityChangeReceiver;
import solutions.masai.masai.android.core.helper.RealmHelper;
import solutions.masai.masai.android.core.model.Host;
import solutions.masai.masai.android.core.model.Message;
import solutions.masai.masai.android.core.model.MessageAttachment;
import solutions.masai.masai.android.core.model.SyncState;
import solutions.masai.masai.android.core.model.rocketchat.Room;

import java.util.List;

public class ChatController extends ChatLocationController implements ChatView.ChatViewListener, ChatAdapter.OnChatAdapterListener, HostConnection.IHostConnectionCallback, ConnectivityChangeReceiver.INetworkState {
    private ChatAdapter chatAdapter;
    private ChatView view;
    private boolean shouldTryAgain = true;
    private int connectionPositionOnList;
    private Handler handler = new Handler(Looper.getMainLooper());
    private ConnectivityChangeReceiver connectivityChangeReceiver;

    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        view = new ChatView(findViewById(android.R.id.content), this);
        retrievePos();
        initRoom();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view.setTitle(ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost().getHumanName());
        boolean showPermissionRequest = getIntent().getBooleanExtra(C.EXTRA_SHOW_PERMISSION_REQUEST, true);
        chatAdapter = new ChatAdapter(this, connectionPositionOnList, room.getId(), showPermissionRequest, this);
        view.setChatListAdapter(chatAdapter);
        view.updateList(chatAdapter);
        view.showProgress();
        connectOrReconnect();
    }

    @Override
    public void onDisconnected() {
        C.L("Host "+connectionPositionOnList + " on disconnected");
        ConnectionManager.setShouldShowInfoReconnecting(true);
        if (shouldTryAgain) {
            shouldTryAgain = false;
            connectOrReconnect();
        } else {
            view.hideProgress();
        }
    }

    @Override
    public void onConnectedAndLoggedIn() {
        shouldTryAgain = true;
        view.hideProgress();
        if (ConnectionManager.isShouldShowInfoReconnecting()) {
            view.showInfoReconnecting();
            ConnectionManager.setShouldShowInfoReconnecting(false);
        }
        loadMissedMessagesAndSubscribeToRoom();
    }

    private void searchForDuplicateFileMessagesAndRemoveLocal() {
        RealmHelper.getInstance().removeDuplicateImageMessages(room.getId(), () -> handler.post(() -> {
            view.updateList(chatAdapter);
            view.scrollToBottom(chatAdapter);
        }));
    }

    @Override
    public void onNewMessage(Room room) {
        C.L("FLEFLEonNewMessage");
        handler.post(() -> {
            if(room.isClosed()) {
                view.hideMessageInput();
            }
            searchForDuplicateFileMessagesAndRemoveLocal();
            view.updateList(chatAdapter);
            view.scrollToBottom(chatAdapter);
        });
    }

    @Override
    public void onHostReady() {
        C.L("Host ready!");
    }

    @Override
    public void onHostNotReachable() {
        C.L("Host not active!");
    }

    @Override
    public void onRoomCreated(String roomId) {
        C.L("Room "+roomId+" created!");
        handler.post(() -> {
            searchForDuplicateFileMessagesAndRemoveLocal();
            view.updateList(chatAdapter);
            view.scrollToBottom(chatAdapter);
        });
    }

    @Override
    public void onConnectivityError() {
        C.L("Host "+connectionPositionOnList + " connectivity error");
        view.hideProgress();
    }

    HostConnection.ISendFileCallback filesCallback = new HostConnection.ISendFileCallback() {
        @Override
        public void onFileSent(final String name, final String filePath) {
            C.L("onFileSent");
            chatAdapter.setTemporaryImagePath(filePath, name);
            handler.post(() -> view.hideImageLoadingMessage());
        }

        @Override
        public void onImageAddedToDatabase() {
            clearImagePath();
            view.updateList(chatAdapter);
            view.scrollToBottom(chatAdapter);
        }

        @Override
        public void onFileTooBigError(final String filePath) {
            C.L("FILEUPLOAD onFileTooBigError");
            RealmHelper.getInstance().removeMessageWithId(room.getId(), filePath);
            handler.post(() -> {
                view.hideImageLoadingMessage();
                view.showFileTooBigError();
                view.updateList(chatAdapter);
            });
        }

        @Override
        public void onConnectionError(final String filePath) {
            C.L("FILEUPLOAD onConnectionError");
            handler.post(() -> {
                RealmHelper.getInstance().updateMessageState(room.getId(), filePath, SyncState.FAILED);
                view.updateList(chatAdapter);
                view.hideImageLoadingMessage();
                view.showUploadFailed();
            });
        }

        @Override
        public void onInvalidFileType(final String filePath) {
            C.L("FILEUPLOAD onInvalidFileType");
            handler.post(() -> {
                RealmHelper.getInstance().removeMessageWithId(room.getId(), filePath);
                view.updateList(chatAdapter);
                view.hideImageLoadingMessage();
                view.showInvalidFileType();
            });
        }

        @Override
        public void onServerError(final String filePath) {
            C.L("FILEUPLOAD onServerError");
            handler.post(() -> {
                RealmHelper.getInstance().updateMessageState(room.getId(), filePath, SyncState.FAILED);
                view.updateList(chatAdapter);
                view.hideImageLoadingMessage();
                view.showServerError();
            });
        }
    };

    HostConnection.ISendMessagesCallback messagesCallback = new HostConnection.ISendMessagesCallback() {
        @Override
        public void onMessageStateUpdated() {
            C.L("message updated");
            view.updateList(chatAdapter);
            view.scrollToBottom(chatAdapter);
        }

        @Override
        public void onConnectivityError() {
            C.L("message error");
            view.updateList(chatAdapter);
        }
    };

    @Override
    protected void onPause() {
        C.L("onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        C.L("Chatcontroller onStop");
        App.setClearHostConnections(true);
        handler.removeCallbacksAndMessages(null);
        if (connectivityChangeReceiver != null) {
            unregisterReceiver(connectivityChangeReceiver);
        }
    }

    @Override
    protected void onResume() {
        connectivityChangeReceiver = ConnectivityChangeReceiver.registerNetworkStateReceiver(this, this);
	    super.onResume();
    }

    private void connectOrReconnect() {
        C.L("Connecting or reconnecting to hosts...");
        List<Host> hostList = ConnectionManager.getInstance().getHostList();
        for(Host host : hostList) {
            if(!ConnectionManager.getInstance().isHostConnectionActive(host)) {
                ConnectionManager.getInstance().addHostConnection(hostList.get(hostList.indexOf(host)));
            }
        }
        ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).connectIfDisconnectedAndSetHostListener(this,
                this);
    }

    private void retrievePos() {
        connectionPositionOnList = getIntent().getIntExtra(C.EXTRA_HOST_CONNECTION_POSITION, 0);
    }

    private void initRoom() {
        String roomId = getIntent().getStringExtra(C.EXTRA_ROOM_ID);
        room  = ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost().getRoomById(roomId);
        if(room.isClosed()) {
            view.hideMessageInput();
        }
    }

    private void sendFile(final String filePath) {
        ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).requestFileSpaceAndUpload(room.getId(), filePath,
                filesCallback);
    }

    private void loadMissedMessagesAndSubscribeToRoom() {
        if(room != null) {
            handler.post(() -> {
                C.L("loadMissedMessagesAndSubscribeToRoom " + room.getId());
                ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).loadMessagesFromWhileIWasAway(
                        room.getId(), new HostConnection.IGetMessagesCallback() {
                    @Override
                    public void onMessagesReady() {
                        view.updateList(chatAdapter);
                        resendUnsynchronized();
                        scrollToPosition();
                    }

                    @Override
                    public void onConnectivityError() {
                        C.L("loadMissedMessagesAndSubscribeToRoom for host " + connectionPositionOnList + " connectivity error");
                    }
                });
            });
        }
    }

    private void scrollToPosition() {
        String searchTerm = getIntent().getStringExtra(C.EXTRA_SEARCH_TERM);
        if(searchTerm != null) {
            List<Message> messages = RealmHelper.getInstance().getMessages(room.getId());
            RealmHelper.getInstance().executeCustomTransaction(realm -> {
                boolean hasScrolled = false;
                for(Message message : messages) {
                    message.setSearched(false);
                    if(message.getMessage().toUpperCase().contains(searchTerm.toUpperCase())) {
                        message.setSearched(true);
                        if(!hasScrolled) {
                            int position = messages.indexOf(message);
                            view.scrollToPosition(chatAdapter, position);
                            hasScrolled = true;
                        }
                    }
                }
            });
        } else {
            view.scrollToBottom(chatAdapter);
        }
    }

    private void resendUnsynchronized() {
        ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).resendUnsynchronizedMessages(room.getId(),
                messagesCallback, filesCallback);
    }

    @Override
    public void onMessageSendClicked(final String message) {
        ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).sendMessage(null, room.getId(), message, 0, 0,
                messagesCallback);
    }

    @Override
    public void onNavigateBack() {
        RealmHelper.getInstance().unsetSearchedForMessages(room.getId());
        onBackPressed();
    }

    @Override
    public void onMessageClicked(final int messagePosition) {
        final String token = ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost().getRcUser()
                .getUserId();
        final String userId = ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost().getRcUser()
                .getUserId();
        handleMessageClick(this, messagePosition, token, userId);
    }

    private MessageAttachment tmpAttachment;
    private String tmpToken;
    private String tmpUserId;

    public void handleMessageClick(final Context context, final int messagePos, final String token, final String userId) {
        C.L("handeling message Click");
        List<Message> messageList = RealmHelper.getInstance().getMessages(room.getId());
        if (messagePos <= messageList.size()) {
            final Message message = messageList.get(messagePos);
            String filePath = null;
            if (message != null) {
                if (message.getAttachments() != null) {
                    for (MessageAttachment attachment : message.getAttachments()) {
                        if (attachment.getTitle().contains(C.TITLE_VALUE_FILE) && !attachment.getTitleUrl().isEmpty()) {
                            filePath = attachment.getTitleUrl();
                        } else {
                            tmpAttachment = attachment;
                            tmpToken = token;
                            tmpUserId = userId;
                            requestPermissionToDownloadOrOpenAttachment();
                        }
                    }
                }
                if (message.getLocation() != null) {
                    ChatUtilities.startMap(this, message.getLocation().getLat(), message.getLocation().getLng(), "");
                }
            }
        }
    }

    @Override
    public void downloadOrOpenAttachment() {
        if (tmpAttachment != null && tmpUserId != null && tmpToken != null) {
            ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList)
                    .downloadOrOpenFileFromAttachment(ChatController.this, tmpAttachment, tmpToken, tmpUserId);
        }
        tmpAttachment = null;
        tmpToken = null;
        tmpUserId = null;
    }

    @Override
    public void onAddClicked() {
        view.showAddOptions();
    }

    @Override
    public void onTakePhotoClicked() {
        App.setClearHostConnections(false);
        takePhoto();
    }

    @Override
    public void onSelectPhotoFromGalleryClicked() {
        App.setClearHostConnections(false);
        selectPhotoFromGallery();
    }

    @Override
    public void onSelectFileClicked() {
        App.setClearHostConnections(false);
        selectFile();
    }

    @Override
    public void fileSelected(final String filePath) {
        if (filePath != null) {
            if (!filePath.startsWith("http")) {
                view.showImageLoadingMessage();
                handler.post(() -> sendFile(filePath));
            } else {
                view.showImageIsNotLocalError();
            }
        } else {
            view.showImageError();
        }
    }

    @Override
    public void onShareLocationClicked() {
        requestLocationAccess();
    }

    @Override
    public void locationAccessGranted() {
        startMap();
    }

    @Override
    public void shareLocation(LatLng latestPosition) {
        if (latestPosition != null) {
            ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList)
                    .sendMessage(null, room.getId(), "", (float) latestPosition.latitude, (float) latestPosition.longitude, messagesCallback);
        }
    }

    @Override
    public void onImageLoaded() {
        if (view.getNumberOfNewMessages() == 1 && view.isLastPosVisible()) {
            view.updateList(chatAdapter);
            view.scrollToBottom(chatAdapter);
        }
    }

    @Override
    public void onStateConnected() {
        connectOrReconnect();
    }

    @Override
    public void onStateDisconnected() {
        //not used here
    }
}
