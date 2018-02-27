package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import common.MessageENT;

public class AMSMessage extends BodyTagSupport {

	String errorMessage = "";
	String successMessage = "";
	MessageENT messageEntity;

	public AMSMessage() {
		super();
	}

	public int doStartTag() throws JspException {
		int res = super.doStartTag();
		try {
			pageContext.getOut().write(loadHeaderTitle());
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
			throw new JspException("error in AIPPagination: " + ex);
		}
		return super.doAfterBody();
	}

	public int doEndTag() throws JspException {
		int i = super.doEndTag();
		try {
			pageContext.getOut().write("");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return i;
	}

	String loadHeaderTitle() {
		String sb = "";
		try {
			String realPath = pageContext.getServletContext().getRealPath(
					"images/skin/message/amsskin_message.html");
			File f = new File(realPath);
			FileInputStream fin = new FileInputStream(f);
			byte buf[] = new byte[fin.available()];
			fin.read(buf);
			fin.close();
			sb = new String(buf);
			String temp = "<label id='errorDescription' style='color:red;'>"
					+ getErrorMessage() + "</label>" + "<br>"
					+ "<label id='successDescription' style='color:green;'>"
					+ getSuccessMessage() + "</label>";
			sb = AMSUtililies.replace(sb, "[CONTENT]", temp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public MessageENT getMessageEntity() {
		return messageEntity;
	}

	public void setMessageEntity(MessageENT message) {
		if(message == null)
			message = new MessageENT("", "");
		setErrorMessage(message.getError());
		setSuccessMessage(message.getSuccess());
		this.messageEntity = message;
	}

}