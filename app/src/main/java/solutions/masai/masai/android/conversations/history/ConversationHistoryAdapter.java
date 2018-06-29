package solutions.masai.masai.android.conversations.history;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.conversations.base.BaseConversationAdapter;
import solutions.masai.masai.android.conversations.base.BaseConversationViewHolder;
import solutions.masai.masai.android.core.model.rocketchat.Room;

/**
 * Created by cWahl on 15.11.2017.
 */

public class ConversationHistoryAdapter extends BaseConversationAdapter {

	public ConversationHistoryAdapter(Context context, ConversationRoomCallback callback) {
		super(context, callback);
	}

	@Override
	public BaseConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = inflater.inflate(R.layout.item_connection_list, parent, false);
		return new ConversationHistoryViewHolder(view, callback);
	}

	public void setRooms(List<Room> rooms) {
		Collections.sort(rooms, (r1, r2) -> r1.getHostHumanName().compareTo(r2.getHostHumanName()));
		this.rooms.clear();
		this.rooms.addAll(rooms);
		this.notifyDataSetChanged();
	}
}
