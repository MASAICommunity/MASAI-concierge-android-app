package solutions.masai.masai.android.profile.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.ViewHelper;
import solutions.masai.masai.android.profile.controller.BaseController;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;

/**
 * Created by cWahl on 25.08.2017.
 */

public class BaseView implements View.OnFocusChangeListener {

	public View rootView;
	public Context context;
	public ProgressDialog progressDialog;
	private String actionbarTitle;

	public BaseView(View rootView, Context context, String actionbarTitle) {
		this.rootView = rootView;
		this.context = context;
		this.actionbarTitle = actionbarTitle;
		initViews();
	}

	private void initViews() {
		progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(rootView.getContext().getString(R.string.loading_data));
		progressDialog.setCanceledOnTouchOutside(false);

		final Drawable upArrow = context.getDrawable(R.drawable.ic_arrow_back);
		upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

		((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((AppCompatActivity) context).getSupportActionBar().setHomeAsUpIndicator(upArrow);
		((AppCompatActivity) context).setTitle(actionbarTitle);
		((AppCompatActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	public void onFocusChange(View view, boolean isFocused) {
		ViewHelper.EDIT_TEXT_STATE state;

		if (!isFocused) {
			if (((EditTextRichDrawable) view).getText().toString().length() > 0) {
				state = ViewHelper.EDIT_TEXT_STATE.OK;
			} else {
				state = ViewHelper.EDIT_TEXT_STATE.NOT_FOCUSED;
			}
		} else {
			state = ViewHelper.EDIT_TEXT_STATE.FOCUSED;
			BaseController.setLastFocusedView(view);
		}

		ViewHelper.setEditTextDrawableState(context, view, state);
	}

	public void clearCurrentFocus() {
		View view = ((Activity) context).getCurrentFocus();
		if(view != null) {
			view.clearFocus();
		}
	}
}
