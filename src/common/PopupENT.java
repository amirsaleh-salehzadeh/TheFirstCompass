package common;

public class PopupENT {
	String id = "";
	String onClick = "";
	String value = "";
	String href = "#";

	public PopupENT(String id, String onClick, String value, String href) {
		this.id = id;
		this.onClick = onClick;
		this.value = value;
		this.href = href;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}
