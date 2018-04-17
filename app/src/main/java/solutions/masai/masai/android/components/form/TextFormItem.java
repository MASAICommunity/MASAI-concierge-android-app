package solutions.masai.masai.android.components.form;

/**
 * Created by tom on 01.09.17.
 */

public class TextFormItem {

	private String title;
	private String text;

	public TextFormItem(String title) {
		this.title = title;
	}

	public TextFormItem(String title, String text) {
		this.title = title;
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
