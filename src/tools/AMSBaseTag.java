package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;


public class AMSBaseTag extends BodyTagSupport {

	

	/*
	 * Attributes
	 */
	//required
	//optoinal
	protected String title="";
	protected String styleId="id1";
	protected String align="right";
	protected String style="";

	/*
	 * attribute placeholders
	 */
	protected String styleIdPlaceHolder="[STYLEIDPLACEHOLDER]";
	protected String titlePlaceHolder="[TITLEPLACEHOLDER]";
	protected String alignPlaceHolder="[ALIGNPLACEHOLDER]";


	/*
	 * required to initialized in Constructors
	 */
	protected String baseFolder="tools";
	//style="simple"; for example
	/*
	 * internal variables
	 */
	
	protected String jqueryPath="	<script type=\"text/javascript\" src=\"js/jquery-1.1.4.js\"></script><script type=\"text/javascript\" src=\"js/jquery.tree.js\"></script>";
	protected String contentStartTag="",contentEndTag="";
	protected long lastCheckTime,lastFileReaded;
	protected int delayTime = 5000; // 65000;
	protected String oldstyle="";

//	static protected Hashtable htContentStartTag=new Hashtable();  
//	static protected Hashtable htContentEndTag=new Hashtable();  
	
	/*
	 * internal placeholders
	 */
	protected String placeHolder="[PLACEHOLDER]";
	protected String jqueryPlaceHolder="[JQUERYPLACEHOLDER]";

	public int doAfterBody() throws JspException {
		try {
			BodyContent bodyContent = getBodyContent();
			JspWriter out = bodyContent.getEnclosingWriter();
			bodyContent.writeOut(out);
			bodyContent.clearBody();
		} catch (Exception ex) {
			throw new JspException("error in AMSBaseTag: " + ex);
		}
		return super.doAfterBody();
	}

	public int doEndTag() throws JspException {
		int i = super.doEndTag();
		try {
			
			pageContext.getOut().write(getContentEndTag());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return i;
	}

	
	protected void loadContentFile(String contentFile){
		if((lastCheckTime+delayTime)<System.currentTimeMillis() || !style.equalsIgnoreCase(oldstyle) )
		{
			lastCheckTime=System.currentTimeMillis();
			try {
				String realPath = pageContext.getServletContext().getRealPath(contentFile );
				File f = new File(realPath);
				if(f.lastModified()>lastFileReaded || !style.equalsIgnoreCase(oldstyle) )
				{

					FileInputStream fin = new FileInputStream(f);
					byte buf[]=new byte[fin.available()];
					fin.read(buf);
					fin.close();
					StringBuffer sb = new StringBuffer( new String(buf,"utf8") );


					/*
					 * split start and end
					 */
					int pos = sb.indexOf(placeHolder);
					contentStartTag = sb.substring(0,pos);
					contentEndTag = sb.substring(pos+placeHolder.length());
					
					lastFileReaded=f.lastModified();
	
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			oldstyle=style;
		}
	}



	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}



	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}



	/**
	 * @return the styleId
	 */
	public String getStyleId() {
		return styleId;
	}



	/**
	 * @param styleId the styleId to set
	 */
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}



	/**
	 * @return the align
	 */
	public String getAlign() {
		return align;
	}



	/**
	 * @param align the align to set
	 */
	public void setAlign(String align) {
		this.align = align;
	}



	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}



	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	public String getContentStartTag() {
		return contentStartTag;
	}

	public String getContentEndTag() {
		return contentEndTag;
	}


}
