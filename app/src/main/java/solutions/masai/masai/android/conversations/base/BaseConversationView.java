package solutions.masai.masai.android.conversations.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.conversations.conversation.ConversationsController;
import solutions.masai.masai.android.conversations.history.ConversationHistoryController;

/**
 * Created by cWahl on 15.11.2017.
 */

public abstract class BaseConversationView {

	protected View rootView;
	protected BaseConversationController activity;
	protected RecyclerView rvRooms;
	protected TextView tvEmptyScreen;

	public BaseConversationView(BaseConversationController activity) {
		this.activity = activity;
		this.rootView = activity.findViewById(android.R.id.content);
		initView();
	}

	protected void initView() {
		if(activity instanceof ConversationHistoryController) {
			rvRooms = (RecyclerView) rootView.findViewById(R.id.activity_chat_history_room_list);
			tvEmptyScreen = (TextView) rootView.findViewById(R.id.activity_chat_history_empty_screen);
		} else if(activity instanceof ConversationsController) {
			rvRooms = (RecyclerView) rootView.findViewById(R.id.activity_main_connection_list);
			tvEmptyScreen = (TextView) rootView.findViewById(R.id.content_main_empty_screen);
		}
	}
}
