package solutions.masai.masai.android.conversations.history;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.List;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.conversations.base.BaseConversationController;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.RealmHelper;
import solutions.masai.masai.android.core.model.rocketchat.Room;
import solutions.masai.masai.android.databinding.ActivityChatHistoryBinding;

/**
 * Created by cWahl on 15.11.2017.
 */

public class ConversationHistoryController extends BaseConversationController
		implements ConversationHistoryView.ConversationHistoryViewListener {

	private ConversationHistoryView view;
	private String searchTerm;
	private ConversationHistoryView.SearchCategory searchCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityChatHistoryBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_history);
		view = new ConversationHistoryView(this, this, binding);
		showAllRooms();
	}

	@Override
	public void initToolbar(Toolbar toolbar) {
		toolbar.setNavigationOnClickListener(v -> onBackPressed());
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(R.string.nav_conversation_history);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onRoomSelected(Room room) {
		if(searchCategory == ConversationHistoryView.SearchCategory.TEXT) {
			startChat(room.getHostConPosition(), room, false, searchTerm);
		} else {
			startChat(room.getHostConPosition(), room, false);
		}
	}

	@Override
	public void onSearch(ConversationHistoryView.SearchCategory searchCategory, String keyword) {
		C.L("Searching " + keyword);
		searchTerm = keyword;
		this.searchCategory  = searchCategory;
		List<Room> searchResult;
		if (searchCategory == ConversationHistoryView.SearchCategory.TEXT) {
			searchResult = RealmHelper.getInstance().searchHistoryRoomsByTerm(keyword);
		} else {
			searchResult = RealmHelper.getInstance().searchHistoryRoomsByDate(keyword);
		}

		if (searchResult != null) {
			view.showData(searchResult);
		}
	}

	@Override
	public void showAllRooms() {
		searchTerm = null;
		searchCategory = null;
		handler.post(() -> {
			List<Room> closedRooms = ConnectionManager.getInstance().getAllClosedRooms();
			view.showData(closedRooms);
		});
	}
}
