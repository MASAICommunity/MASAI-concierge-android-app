package solutions.masai.masai.android.conversations.conversation;

import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;

import com.none.emojioneandroidlibrary.Emojione;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.chat.ChatUtilities;
import solutions.masai.masai.android.conversations.base.BaseConversationAdapter;
import solutions.masai.masai.android.conversations.base.BaseConversationViewHolder;
import solutions.masai.masai.android.core.helper.RealmHelper;
import solutions.masai.masai.android.core.helper.TimeHelper;
import solutions.masai.masai.android.core.model.Message;
import solutions.masai.masai.android.core.model.MessageAttachment;
import solutions.masai.masai.android.core.model.PermissionQuery;
import solutions.masai.masai.android.core.model.rocketchat.Room;

import static solutions.masai.masai.android.App.getContext;

/**
 * Created by cWahl on 06.11.2017.
 */

public class ConversationRoomViewHolder extends BaseConversationViewHolder {

	public ConversationRoomViewHolder(View itemView, BaseConversationAdapter.ConversationRoomCallback callback) {
		super(itemView);
		view.conversationItemContainer.setOnClickListener(v -> callback.onRoomClicked(room));
	}

	@Override
	public void bind(Room room) {
		super.bind(room);

		if (room != null && room.getMessagesCounter() > 0) {
			Message latestMessage = RealmHelper.getInstance().getLatestMessage(room.getId());
			Object[] trainsOrPlaces = null;
			boolean isPermissionRequest = false;
			Spannable messageText = new SpannableString("");
			if (latestMessage != null) {
				String whosMessage = ChatUtilities.getAppropriateMessageSender(latestMessage);
				if (latestMessage.getMessage().startsWith("{") || latestMessage.getMessage().startsWith("[")) {
					trainsOrPlaces = ChatUtilities.getTrains(latestMessage.getMessage());
					if (trainsOrPlaces == null) {
						trainsOrPlaces = ChatUtilities.getGooglePlaces(latestMessage.getMessage());
					}
					PermissionQuery permissionQuery = ChatUtilities.getPermissionQuery(latestMessage.getMessage());
					if (permissionQuery != null) {
						isPermissionRequest = true;
					}
				}
				if (trainsOrPlaces != null && trainsOrPlaces.length > 0) {
					messageText = new SpannableString(getContext().getText(R.string.suggested_connections).toString());
				} else if (isPermissionRequest) {
					messageText = new SpannableString(getContext().getText(R.string.request_travelfolder_permission).toString());
				} else if (latestMessage.getLocation() != null) {
					messageText = new SpannableString(String.format(getContext().getString(R.string.user_shared_location), whosMessage));
				} else if (latestMessage.hasAttachments() && latestMessage.isContentEmpty()) {
					for (MessageAttachment messageAttachment : latestMessage.getAttachments()) {
						if (messageAttachment.getImageUrl() != null && !messageAttachment.getImageUrl().isEmpty()) {
							messageText = new SpannableString(String.format(getContext().getString(R.string.user_shared_photo),
									whosMessage));

						} else if (messageAttachment.getTitleLink() != null && !messageAttachment.getTitleLink().isEmpty()) {
							messageText = new SpannableString(String.format(getContext().getString(R.string.user_shared_file),
									whosMessage));
						}
					}
				}
				if (messageText.length() == 0) {
					messageText = Emojione.replaceAll(getContext(), latestMessage.getMessage(), false);
				}
			}

			if (latestMessage != null) {
				view.itemConnectionListLastMessage.setText(messageText);
				view.itemConnectionListLastMessageTime.setText(TimeHelper.getTimeAgo(getContext(), latestMessage.getTimeStamp()));
				view.itemConnectionListLastMessage.setVisibility(View.VISIBLE);
				view.itemConnectionListLastMessageTime.setVisibility(View.VISIBLE);
			} else {
				view.itemConnectionListLastMessage.setVisibility(View.INVISIBLE);
				view.itemConnectionListLastMessageTime.setVisibility(View.INVISIBLE);
			}
		}
	}
}