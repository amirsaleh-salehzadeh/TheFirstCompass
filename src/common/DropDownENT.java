package common;

public class DropDownENT {
	String value;
	String text;
	String onClick;

	public DropDownENT(String value, String text, String onClick) {
		super();
		this.value = value;
		this.text = text;
		this.onClick = onClick;
	}

	public String getOnClick() {
		return onClick;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
