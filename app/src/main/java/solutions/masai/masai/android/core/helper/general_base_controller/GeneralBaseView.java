package solutions.masai.masai.android.core.helper.general_base_controller;

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
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;

/**
 * @author Sebastian Tanzer
 * @version 1.0
 *          created on 05/09/2017
 */

public abstract class GeneralBaseView implements View.OnFocusChangeListener{

    public View rootView;
    public Context context;
    public ProgressDialog progressDialog;
    public String title;

    public GeneralBaseView(View rootView, Context context) {
        this.rootView = rootView;
        this.context = context;
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
        ((AppCompatActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void setTitle(String title){
        /*SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
        ((AppCompatActivity) context).setTitle(title);
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
        }

        ViewHelper.setEditTextDrawableState(context, view, state);
    }

}

