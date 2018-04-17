package solutions.masai.masai.android.core.helper;

import android.content.Context;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import solutions.masai.masai.android.core.model.Host;
import solutions.masai.masai.android.core.model.Message;
import solutions.masai.masai.android.core.model.MessageAttachment;
import solutions.masai.masai.android.core.model.SyncState;
import solutions.masai.masai.android.core.model.realm.RocketChatUser;
import solutions.masai.masai.android.core.model.rocketchat.Room;

/**
 * Created by Semko on 2016-12-06.
 */

public class RealmHelper {
    private static RealmHelper instance;

    public static RealmHelper getInstance() {
        if (instance == null) {
            instance = new RealmHelper();
        }
        return instance;
    }

    public void init(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }

    private long getNextKey(Realm realm, Class c) {
        Number mid = realm.where(c).max("id");
        if (mid != null) {
            return mid.longValue() + 1;
        }
        return 0;
    }

    public void drop() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

    public void executeCustomTransaction(Realm.Transaction transaction) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(transaction);
    }

    public void removeAllHosts() {
        for(Host host : getHosts()) {
            removeHost(host.getHostRocketChatApiUrl());
        }
    }

    public void dropRocketChatUsers() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RocketChatUser> rcUsers = realm.where(RocketChatUser.class).findAll();
                for(RocketChatUser rcUser : rcUsers) {
                    rcUser.deleteFromRealm();
                }
            }
        });
    }

    public void dropMessages() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Message> messages = realm.where(Message.class).findAll();
                for(Message message : messages) {
                    message.deleteFromRealm();
                }
            }
        });
    }

    public void removeHost(final String rocketChatUrl) {
        C.L("Removing host " + rocketChatUrl + " from db...");
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Host host = realm.where(Host.class).equalTo("hostRocketChatApiUrl", rocketChatUrl).findFirst();
                if (host != null) {
                    host.deleteFromRealm();
                }
            }
        });
    }

    public Host insertOrUpdateHost(final Host host) {
        C.L("DB: Insert/Update host " + host.getHostRocketChatApiUrl());
        try(Realm realm = Realm.getDefaultInstance()) {
            Host realmHost = realm.where(Host.class).equalTo("hostRocketChatApiUrl", host.getHostRocketChatApiUrl()).findFirst();

            if(realmHost != null) {
                realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(host));
            } else {
                realm.beginTransaction();
                Host newHost = realm.createObject(Host.class, host.getHostRocketChatApiUrl());
                newHost.setHumanName(host.getHumanName());
                newHost.setHostUrl(host.getHostUrl());
                newHost.setLiveChatToken(host.getLiveChatToken());
                realm.commitTransaction();
            }

            return realmHost;
        }
    }

    public List<Host> getHosts() {
        try(Realm realm = Realm.getDefaultInstance()) {
            List<Host> hosts = new ArrayList<>();
            final RealmResults<Host> hostResults = realm.where(Host.class).findAll();
            if (hostResults != null) {
                for(Host localhost : hostResults) {
                    hosts.add(realm.copyFromRealm(localhost));
                }
            }
            return hosts;
        }
    }

    public RocketChatUser getRocketChatUserForHost(Host host) {
        try(Realm realm = Realm.getDefaultInstance()) {
            final RocketChatUser rcUser = realm.where(RocketChatUser.class).equalTo("rocketchatUrl",
                    host.getHostRocketChatApiUrl()).findFirst();

            return rcUser == null ? rcUser : realm.copyFromRealm(rcUser);
        }
    }

    public List<Room> searchHistoryRoomsByTerm(String searchTerm) {
        try(Realm realm = Realm.getDefaultInstance()) {
            List<Room> result = new ArrayList<>();
            RealmResults<Message> messages = realm.where(Message.class).contains(C.JSON_FIELD_MESSAGE, searchTerm, Case.INSENSITIVE)
            .findAll();
            List<String> roomIds = new ArrayList<>();
            for(Message message : messages) {
                if(!message.getMessage().contains(C.BASE_AWS_URL) && !roomIds.contains(message.getRoomId())) {
                    roomIds.add(message.getRoomId());
                }
            }
            RealmResults<Host> hosts = realm.where(Host.class).findAll();
            for(Host host : hosts) {
                for(Room room : host.getClosedRooms()) {
                    if(roomIds.contains(room.getId())) {
                        result.add(room);
                    }
                }
            }

            return result;
        }
    }

    public void unsetSearchedForMessages(String roomId) {
        List<Message> messages = RealmHelper.getInstance().getMessages(roomId);
        RealmHelper.getInstance().executeCustomTransaction(realm -> {
            for(Message message : messages) {
                if(message.isSearched()) {
                    message.setSearched(false);
                }
            }
        });
    }

    public List<Room> searchHistoryRoomsByDate(String date) {
        try(Realm realm = Realm.getDefaultInstance()) {
	        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());
            Date searchdate;
            try {
                searchdate = df.parse(date);
            } catch(ParseException ex) {
                C.L("Date parsing error: "+ex.getMessage());
                return null;
            }

            List<Room> result = new ArrayList<>();
            RealmResults<Host> hosts = realm.where(Host.class).findAll();
            for(Host host : hosts) {
                for(Room room : host.getClosedRooms()) {
                    final RealmResults<Message> messages = realm.where(Message.class).equalTo(C.JSON_FIELD_ROOM_ID, room.getId())
                            .findAllSorted(C.JSON_FIELD_TIMESTAMP);
                    Message firstMessage = null;
                    Message lastMessage = null;
                    if(messages != null && !messages.isEmpty()) {
                        firstMessage = messages.get(0);
                        lastMessage = messages.get(messages.size()-1);
                    }

                    Date firstDate = null;
                    Date lastDate = null;
                    if(firstMessage != null && lastMessage != null) {
                        firstDate = new Date(firstMessage.getTimeStamp());
                        lastDate = new Date(lastMessage.getTimeStamp());
                    }
                    if(firstDate != null && lastDate != null) {
                        if((searchdate.after(firstDate) && searchdate.before(lastDate))
                            || DateUtils.isSameDay(searchdate, firstDate) || DateUtils.isSameDay(searchdate, lastDate)) {
                            result.add(room);
                        }
                    }
                }
            }
            return result;
        }
    }

    public Host[] getHostConnectionListListener(RealmChangeListener<RealmResults<Host>> listener) {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Host> conversationList = realm.where(Host.class).findAll();
        conversationList.addChangeListener(listener);
        Host[] hosts = conversationList.toArray(new Host[]{});
        return hosts;
    }

    public List<Message> getMessages(String roomId) {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Message> messages = realm.where(Message.class).equalTo(C.JSON_FIELD_ROOM_ID, roomId).findAllSorted(C.JSON_FIELD_TIMESTAMP);
        return messages.subList(0, messages.size());
    }

    public List<Message> getUnsentMessages(String roomId) {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Message> messages = realm.where(Message.class).equalTo(C.JSON_FIELD_ROOM_ID, roomId)
                .equalTo(C.JSON_FIELD_SYNCSTATE, SyncState.FAILED).findAllSorted(C.JSON_FIELD_TIMESTAMP);
        return messages.subList(0, messages.size());
    }

    public Message getFirstMessage(String roomId) {
        C.L("getFirstMessage for Room=" + roomId);
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Message> messages = realm.where(Message.class).equalTo(C.JSON_FIELD_ROOM_ID, roomId).findAllSorted(C.JSON_FIELD_TIMESTAMP);
        if (messages.size() == 0) {
            return null;
        } else {
            return messages.get(0);
        }
    }

    public Message getLatestMessage(String roomId) {
        C.L("getLatestMessage for Room=" + roomId);
        Realm realm = Realm.getDefaultInstance();
	    RealmResults<Message> messages = null;
	    if(roomId != null) {
		    messages = realm.where(Message.class).equalTo(C.JSON_FIELD_ROOM_ID, roomId).findAllSorted(C
				    .JSON_FIELD_TIMESTAMP);

	    }
        if (messages == null || messages.size() == 0) {
            return null;
        } else {
            return messages.get(messages.size() - 1);
        }
    }

    public void removeDuplicateImageMessages(final String roomId, final IMessageRemovedListener listener) {
        C.L("removeDuplicateImageMessages for Room=" + roomId);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                List<String> messageIdsToRemove = new ArrayList<>();
                final RealmResults<Message> messagesWithAttachments = realm.where(Message.class).equalTo(C.JSON_FIELD_ROOM_ID, roomId)
                        .isNotNull(C.JSON_FIELD_ATTACHMENTS).findAll();
                for (Message localFileMessage : messagesWithAttachments) {
                    if (localFileMessage.hasAttachments() && (localFileMessage.getSyncstate() == SyncState.SYNCING || localFileMessage.getSyncstate() == SyncState.FAILED)) {
                        for (MessageAttachment localFileMessageAttachment : localFileMessage.getAttachments()) {
                            String fileName = null;
                            if (localFileMessageAttachment.getImageUrl() != null && !localFileMessageAttachment.getImageUrl().isEmpty()) {
                                String[] pathSegments = localFileMessageAttachment.getImageUrl().split("/");
                                fileName = pathSegments[pathSegments.length - 1];
                            }
                            if (localFileMessageAttachment.getTitleLink() != null && !localFileMessageAttachment.getTitleLink().isEmpty()) {
                                String[] pathSegments = localFileMessageAttachment.getTitleLink().split("/");
                                fileName = pathSegments[pathSegments.length - 1];
                            }
                            if (localFileMessageAttachment.getTitleUrl() != null && !localFileMessageAttachment.getTitleUrl().isEmpty()) {
                                String[] pathSegments = localFileMessageAttachment.getTitleUrl().split("/");
                                fileName = pathSegments[pathSegments.length - 1];
                            }
                            if (fileName != null) {
                                for (Message syncedFileMessage : messagesWithAttachments) {
                                    if (syncedFileMessage.hasAttachments() && (syncedFileMessage.getSyncstate() == SyncState.SYNCED || syncedFileMessage.getSyncstate() == SyncState.NOT_SYNCED)) {
                                        for (MessageAttachment syncedFileMessageAttachment : syncedFileMessage.getAttachments()) {
                                            String syncedUrl = null;
                                            if (syncedFileMessageAttachment.getImageUrl() != null && !syncedFileMessageAttachment.getImageUrl().isEmpty()) {
                                                syncedUrl = syncedFileMessageAttachment.getImageUrl();
                                            }
                                            if (syncedFileMessageAttachment.getTitleLink() != null && !syncedFileMessageAttachment.getTitleLink().isEmpty()) {
                                                syncedUrl = syncedFileMessageAttachment.getTitleLink();
                                            }
                                            if (syncedUrl != null) {
                                                if (syncedUrl.contains(fileName)) {
                                                    if (!messageIdsToRemove.contains(localFileMessage.getId())) {
                                                        messageIdsToRemove.add(localFileMessage.getId());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                for (String messageIdToRemove : messageIdsToRemove) {
                    final RealmResults<Message> conversationList = realm.where(Message.class).equalTo(C.JSON_FIELD_ROOM_ID, roomId).equalTo(C.JSON_FIELD_ID, messageIdToRemove).findAll();
                    for (Message message : conversationList) {
                        message.deleteFromRealm();
                    }
                }
                if (messageIdsToRemove.size() > 0) {
                    listener.messageRemoved();
                }
            }
        });
    }

    public void removeMessageWithId(final String roomId, final String messageID) {
        C.L("updateMessageState for Room=" + roomId);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final RealmResults<Message> conversationList = realm.where(Message.class).equalTo(C.JSON_FIELD_ROOM_ID, roomId).equalTo(C.JSON_FIELD_ID, messageID).findAll();
                for (Message message : conversationList) {
                    message.deleteFromRealm();
                }
            }
        });
    }

    public void addMessageFromJson(final JSONObject json) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateObjectFromJson(Message.class, json);
            }
        });
    }

    public void updateMessageState(final String roomId, final String messageID, final int syncState) {
        C.L("updateMessageState for Room=" + roomId);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final RealmResults<Message> conversationList = realm.where(Message.class).equalTo(C.JSON_FIELD_ROOM_ID, roomId).equalTo(C.JSON_FIELD_ID, messageID).findAll();
                for (Message message : conversationList) {
                    message.setSyncstate(syncState);
                    realm.copyToRealmOrUpdate(message);
                }
            }
        });
    }

    public void updateMessageRequestPermissionState(final String roomId, final String messageID, final boolean state) {
        C.L("updateMessage RequestPermissionState for Room=" + roomId);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final RealmResults<Message> conversationList = realm.where(Message.class).equalTo(C.JSON_FIELD_ROOM_ID, roomId).equalTo(C.JSON_FIELD_ID, messageID).findAll();
                for (Message message : conversationList) {
                    message.setPermissionConfirmed(state);
                    realm.copyToRealmOrUpdate(message);
                }
            }
        });
    }

    public void addMessagesFromJson(final JSONArray jsonArray, int pos) {
        C.L("addMessagesFromJson for POS=" + Integer.toString(pos));
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateAllFromJson(Message.class, jsonArray);
            }
        });
    }

    public interface IMessageRemovedListener {
        void messageRemoved();
    }
}
