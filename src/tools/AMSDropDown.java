package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import common.DropDownENT;

public class AMSDropDown extends BodyTagSupport {
	String popupID;
	static String contentPattern = "";
	List<DropDownENT> dropDownItems;
	String title = "";
	String selectedVal;
	String name = "";
	String onChange = "";
	String id = "";

	public int doStartTag() throws JspException {
		int res = super.doStartTag();
		try {
			String out = loadDropDown();
			out = out.substring(0, out.indexOf(contentPattern));
			pageContext.getOut().write(out);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}

	public int doAfterBody() throws JspException {
		try {
			BodyContent bodyContent = getBodyContent();
			JspWriter out = bodyContent.getEnclosingWriter();
			bodyContent.writeOut(out);
			bodyContent.clearBody();
		} catch (Exception ex) {
			throw new JspException("error in AMSDropDownTag: " + ex);
		}
		return super.doAfterBody();
	}

	public int doEndTag() throws JspException {
		int i = super.doEndTag();
		try {
			String out = loadDropDown();
			out = out.substring(out.indexOf(contentPattern)
					+ contentPattern.length());
			pageContext.getOut().write(out);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return i;
	}

	String loadDropDown() {
		String sb = "";
		try {
			String realPath = pageContext.getServletContext().getRealPath(
					"images/amsdropdown/ams_dropdown.html");
			File f = new File(realPath);
			FileInputStream fin = new FileInputStream(f);
			byte buf[] = new byte[fin.available()];
			fin.read(buf);
			fin.close();
			sb = new String(buf);
			sb = AMSUtililies.replace(sb, "[CONTENT]",
					createDropDown(this.dropDownItems));
			sb = AMSUtililies.replace(sb, "[NAME]", this.name);
			sb = AMSUtililies.replace(sb, "[ID]", "id='"+this.id+"'");
			sb = AMSUtililies.replace(sb, "[ONCHANGE]", "onchange='"+this.onChange+"'");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb;
	}

	private String createDropDown(List<DropDownENT> items) {
		ArrayList<DropDownENT> temp = (ArrayList<DropDownENT>) items;
		String str = "";
		if (!title.equals("")) {
			str += "<option value='0'>" + this.title + "</option>";
		}
		for (int i = 0; i < temp.size(); i++) {
			DropDownENT item = temp.get(i);
			str += "<option ";
			if (item.getOnClick() != null
					&& !item.getOnClick().equalsIgnoreCase(""))
				str += "onclick='" + item.getOnClick() + "'";
			if (this.selectedVal.equalsIgnoreCase(item.getValue()))
				str += " selected='selected'";
			str += " value='" + item.getValue() + "'>" + item.getText()
					+ "</option>";
		}
		return str;
	}

	public String getPopupID() {
		return popupID;
	}

	public void setPopupID(String popupID) {
		this.popupID = popupID;
	}

	public static String getContentPattern() {
		return contentPattern;
	}

	public static void setContentPattern(String contentPattern) {
		AMSDropDown.contentPattern = contentPattern;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AMSDropDown() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title != null && !title.equals(""))
			this.title = title;
		else
			this.title = "";
	}

	public String getSelectedVal() {
		return selectedVal;
	}

	public void setSelectedVal(String selectedVal) {
		if (selectedVal != null && !selectedVal.equals(""))
			this.selectedVal = selectedVal;
		else
			this.selectedVal = "";

	}

	public List<DropDownENT> getDropDownItems() {
		return dropDownItems;
	}

	public void setDropDownItems(List<DropDownENT> dropDownItems) {
		this.dropDownItems = new ArrayList<DropDownENT>();
		this.dropDownItems = dropDownItems;
	}

	public String getOnChange() {
		return onChange;
	}

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}