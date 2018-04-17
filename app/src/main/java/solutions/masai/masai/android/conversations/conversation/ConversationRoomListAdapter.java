package solutions.masai.masai.android.conversations.conversation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.conversations.base.BaseConversationAdapter;
import solutions.masai.masai.android.conversations.base.BaseConversationViewHolder;
import solutions.masai.masai.android.core.model.rocketchat.Room;

/**
 * Created by cWahl on 06.11.2017.
 */

public class ConversationRoomListAdapter extends BaseConversationAdapter {

	public ConversationRoomListAdapter(Context context, ConversationRoomCallback callback) {
		super(context, callback);
	}

	@Override
	public BaseConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = inflater.inflate(R.layout.item_connection_list, parent, false);
		return new ConversationRoomViewHolder(view, callback);
	}

	public void addRoom(final Room room) {
		if (!rooms.contains(room)) {
			rooms.add(room);
			Collections.sort(rooms, (r1, r2) -> r1.getHostHumanName().compareTo(r2.getHostHumanName()));
		}

		this.notifyDataSetChanged();
	}

	public void clearData() {
		rooms.clear();
		this.notifyDataSetChanged();
	}
}
