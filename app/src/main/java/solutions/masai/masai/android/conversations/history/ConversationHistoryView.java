package solutions.masai.masai.android.conversations.history;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.conversations.base.BaseConversationAdapter;
import solutions.masai.masai.android.conversations.base.BaseConversationController;
import solutions.masai.masai.android.conversations.base.BaseConversationView;
import solutions.masai.masai.android.core.model.rocketchat.Room;
import solutions.masai.masai.android.databinding.ActivityChatHistoryBinding;

import static solutions.masai.masai.android.conversations.history.ConversationHistoryView.SearchCategory.DATE;
import static solutions.masai.masai.android.conversations.history.ConversationHistoryView.SearchCategory.TEXT;

/**
 * Created by cWahl on 15.11.2017.
 */

public class ConversationHistoryView extends BaseConversationView implements BaseConversationAdapter.ConversationRoomCallback,
		RadioGroup.OnCheckedChangeListener, ToggleButton.OnClickListener {

	public enum SearchCategory {TEXT, DATE}

	private ConversationHistoryViewListener listener;
	private ConversationHistoryAdapter adapter;
	private ActivityChatHistoryBinding binding;
	private InputMethodManager imm;
	private Calendar myCalendar = Calendar.getInstance();
	private DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
		myCalendar.set(Calendar.YEAR, year);
		myCalendar.set(Calendar.MONTH, monthOfYear);
		myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		binding.activityChatHistorySearchInput.setText(DateFormat.getDateInstance().format(myCalendar.getTime()));
	};

	public ConversationHistoryView(BaseConversationController activity, ConversationHistoryViewListener listener,
	                               ActivityChatHistoryBinding binding) {
		super(activity);
		this.listener = listener;
		this.listener.initToolbar((Toolbar) rootView.findViewById(R.id.activity_chat_history_toolbar));
		this.binding = binding;
		imm = (InputMethodManager) rootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		adapter = new ConversationHistoryAdapter(activity.getApplicationContext(), this);
		rvRooms.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext()));
		rvRooms.setAdapter(adapter);
		initFilter();
	}

	@Override
	public void onRoomClicked(Room room) {
		listener.onRoomSelected(room);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
		for (int childIndex = 0; childIndex < binding.activityChatHistoryToggleSearchCategory.getChildCount(); childIndex++) {
			final ToggleButton view = (ToggleButton) binding.activityChatHistoryToggleSearchCategory.getChildAt(childIndex);
			view.setChecked(view.getId() == checkedId);
		}
	}

	@Override
	public void onClick(View view) {
		if (view instanceof ToggleButton) {
			if (((ToggleButton) view).isChecked()) {
				((RadioGroup) view.getParent()).check(view.getId());
				binding.activityChatHistorySearchInput.setText("");
				listener.showAllRooms();
			} else {
				((ToggleButton) view).setChecked(true);
			}
		}

		SearchCategory currentCategory = binding.activityChatHistoryToggleText.isChecked() ? TEXT : DATE;
		if (currentCategory == DATE) {
			showDatePicker(view);
		} else {
			showKeyboard();
		}
	}

	public void showData(List<Room> rooms) {
		if (rooms.isEmpty()) {
			tvEmptyScreen.setVisibility(View.VISIBLE);
			rvRooms.setVisibility(View.GONE);
		} else {
			tvEmptyScreen.setVisibility(View.GONE);
			rvRooms.setVisibility(View.VISIBLE);
		}
		adapter.setRooms(rooms);
	}

	private void initFilter() {
		binding.activityChatHistorySearchLayout.post(() -> {
			binding.activityChatHistorySearchLayout.setY(-binding.activityChatHistorySearchLayout.getHeight());
			binding.activityChatHistorySearchLayout.setVisibility(View.GONE);
			binding.activityChatHistoryFilter.setOnClickListener(view -> toggleFilter(binding.activityChatHistorySearchLayout));
		});

		binding.activityChatHistoryToggleText.setOnClickListener(this);
		binding.activityChatHistorySearchToggleDate.setOnClickListener(this);
		binding.activityChatHistorySearchInput.setOnClickListener(this);
		binding.activityChatHistorySearchInput.setOnKeyListener((v, keyCode, event) -> {
			if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
				search(v);
				return true;
			}
			return false;
		});
		binding.activityChatHistoryToggleSearchCategory.setOnCheckedChangeListener(this);
		binding.activityChatHistorySearchButton.setOnClickListener(view -> ConversationHistoryView.this.search(view));
	}

	private void toggleFilter(LinearLayout layout) {
		int animFrom;
		int animTo;
		boolean isExpanded;
		if (layout.getVisibility() == View.GONE) {
			animFrom = -layout.getHeight();
			animTo = 0;
			isExpanded = false;
		} else {
			animFrom = 0;
			animTo = -layout.getHeight();
			isExpanded = true;
		}

		ObjectAnimator expandCollapseAnim = ObjectAnimator.ofFloat(layout, "translationY", animFrom, animTo);
		expandCollapseAnim.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				if (!isExpanded) {
					layout.setVisibility(View.VISIBLE);
					showKeyboard();
				}
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (isExpanded) {
					layout.setVisibility(View.GONE);
					hideKeyboard(layout);
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				//not used here
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				//not used here
			}
		});
		expandCollapseAnim.start();
	}

	private void showKeyboard() {
		binding.activityChatHistorySearchInput.setFocusable(true);
		binding.activityChatHistorySearchInput.requestFocusFromTouch();
		imm.showSoftInput(binding.activityChatHistorySearchInput, 0);
	}

	private void hideKeyboard(View view) {
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	private void showDatePicker(View view) {
		new DatePickerDialog(rootView.getContext(), dateSetListener, myCalendar
				.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
				myCalendar.get(Calendar.DAY_OF_MONTH)).show();

		binding.activityChatHistorySearchInput.setFocusable(false);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	private void search(View view) {
		listener.onSearch(binding.activityChatHistoryToggleText.isChecked() ? TEXT : DATE,
				binding.activityChatHistorySearchInput.getText().toString());
		hideKeyboard(view);
	}

	interface ConversationHistoryViewListener {

		void initToolbar(Toolbar toolbar);

		void showAllRooms();

		void onRoomSelected(Room room);

		void onSearch(SearchCategory searchCategory, String keyword);
	}
}
