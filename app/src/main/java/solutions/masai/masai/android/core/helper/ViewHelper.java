package solutions.masai.masai.android.core.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import solutions.masai.masai.android.R;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;

/**
 * Created by cWahl on 24.08.2017.
 */

public class ViewHelper {

	public enum EDIT_TEXT_STATE {
		FOCUSED,
		NOT_FOCUSED,
		OK,
		ERROR
	}

	public static void setEditTextDrawableState(Context context, View view, EDIT_TEXT_STATE state) {
		EditTextRichDrawable field = ((EditTextRichDrawable) view);

		int height = field.getCompoundDrawableHeight();
		int width = field.getCompoundDrawableWidth();

		Drawable iconState = null;
		switch (state) {
		case FOCUSED:
			iconState = ContextCompat.getDrawable(context, R.drawable.icon_editing);
			break;
		case NOT_FOCUSED:
			iconState = ContextCompat.getDrawable(context, R.drawable.icon_edit);
			break;
		case OK:
			iconState = ContextCompat.getDrawable(context, R.drawable.icon_edit_done);
			break;
		case ERROR:
			iconState = ContextCompat.getDrawable(context, R.drawable.icon_warning);
			break;
		}

		Bitmap iconStateBitmap = ((BitmapDrawable) iconState).getBitmap();
		Drawable resizedIconState = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(iconStateBitmap, width, height,
				true));

		field.setCompoundDrawablesWithIntrinsicBounds(null, null, resizedIconState, null);
	}
}
