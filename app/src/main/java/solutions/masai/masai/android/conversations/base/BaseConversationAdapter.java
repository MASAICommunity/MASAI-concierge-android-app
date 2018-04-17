package solutions.masai.masai.android.conversations.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import solutions.masai.masai.android.core.model.rocketchat.Room;

/**
 * Created by cWahl on 15.11.2017.
 */

public abstract class BaseConversationAdapter extends RecyclerView.Adapter<BaseConversationViewHolder>{

	protected List<Room> rooms = new ArrayList<>();
	protected final LayoutInflater inflater;
	protected final ConversationRoomCallback callback;

	public BaseConversationAdapter(Context context, ConversationRoomCallback callback) {
		this.inflater = LayoutInflater.from(context);
		this.callback = callback;
	}

	@Override
	public void onBindViewHolder(BaseConversationViewHolder holder, int position) {
		holder.bind(rooms.get(position));
	}

	@Override
	public int getItemCount() {
		return rooms.size();
	}

	public interface ConversationRoomCallback {

		void onRoomClicked(Room room);
	}
}
