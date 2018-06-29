package solutions.masai.masai.android.conversations.base;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import solutions.masai.masai.android.core.model.rocketchat.Room;
import solutions.masai.masai.android.databinding.ItemConnectionListBinding;

/**
 * Created by cWahl on 15.11.2017.
 */

public abstract class BaseConversationViewHolder extends RecyclerView.ViewHolder {

	protected ItemConnectionListBinding view;
	protected Room room;

	public BaseConversationViewHolder(View itemView) {
		super(itemView);
		view = DataBindingUtil.bind(itemView);
	}

	public void bind(Room room) {
		this.room = room;
		view.itemConnectionListName.setText(room.getHostHumanName());
		view.conversationItemLogoText.setText(room.getHostHumanName().substring(0, 2));
	}
}
