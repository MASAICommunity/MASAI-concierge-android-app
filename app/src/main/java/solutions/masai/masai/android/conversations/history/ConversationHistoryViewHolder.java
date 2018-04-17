package solutions.masai.masai.android.conversations.history;

import android.databinding.DataBindingUtil;
import android.view.View;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import solutions.masai.masai.android.conversations.base.BaseConversationAdapter;
import solutions.masai.masai.android.conversations.base.BaseConversationViewHolder;
import solutions.masai.masai.android.core.helper.RealmHelper;
import solutions.masai.masai.android.core.model.Message;
import solutions.masai.masai.android.core.model.rocketchat.Room;

/**
 * Created by cWahl on 15.11.2017.
 */

public class ConversationHistoryViewHolder extends BaseConversationViewHolder {

	public ConversationHistoryViewHolder(View itemView, BaseConversationAdapter.ConversationRoomCallback callback) {
		super(itemView);
		view = DataBindingUtil.bind(itemView);
		view.conversationItemContainer.setOnClickListener(v -> callback.onRoomClicked(room));
	}

	@Override
	public void bind(Room room) {
		super.bind(room);
		Message firstMessage = RealmHelper.getInstance().getFirstMessage(room.getId());
		Message latestMessage = RealmHelper.getInstance().getLatestMessage(room.getId());

		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
		if (latestMessage != null && firstMessage != null) {
			view.itemConnectionListLastMessage.setText(df.format(new Date(firstMessage.getTimeStamp())) + " - " +
					df.format(new Date(latestMessage.getTimeStamp())));
			view.itemConnectionListLastMessage.setVisibility(View.VISIBLE);
		} else {
			view.itemConnectionListLastMessage.setVisibility(View.INVISIBLE);
		}
	}
}
