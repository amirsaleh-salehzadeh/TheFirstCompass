package tools;

import javax.servlet.jsp.JspException;


public class AMSDialog extends AMSBaseTag {

	/*
	 * Attributes
	 */
//	String img=baseFolder+"/dialog/window_dialog.gif";
	String img=baseFolder+"/dialog/window_dialog.gif";
	String height="";//
	String screenPosition="";
	String loader="";
	String isIncludeJQuery="true";
	
	String toolbar="<input type='button' value='تایید' id='confirmButtonDialog' onclick='closeDialog(\"dialog1\");[ONOKCLICKPLACEHOLDER]'/><input type='button' id='returnButtonDialog' value='انصرا�?' onclick='closeDialog(\"dialog1\");'/>";
	String toolbarExtra="";
	String onOKClick="";

	
	/*
	 * attribute placeholders
	 */
	String imgPlaceHolder="[IMGPLACEHOLDER]";
	String heightPlaceHolder="[HEIGHTPLACEHOLDER]";
	String screenPositionPlaceHolder="[SCREENPOSITIONPLACEHOLDER]";
	String loaderPlaceHolder="[LOADERPLACEHOLDER]";
	String jqueryPlaceHolder="[JQUERYPLACEHOLDER]";
	
	String toolbarPlaceHolder="[TOOLBARPLACEHOLDER]";
	
	/*
	 * attribute content
	 */
	String jqueryContent="	<script type=\"text/javascript\" src=\"jquery/jquery.js\"></script>" 
		+"<script type=\"text/javascript\" src=\"tools/jquery/ui/ui.core.packed.js\"></script>"
		+"<script type=\"text/javascript\" src=\"tools/jquery/ui/ui.draggable.packed.js\"></script>";
	
	
	public AMSDialog(){
		super();
		style="simple";
		styleId="dialog1";
	}

	public int doStartTag() throws JspException {
		int res = super.doStartTag();
		try {
//			System.out.println("AIPDialog.doStartTag():styleId="+styleId);
//			System.out.println("AIPDialog.doStartTag():title="+title);
//			if(htContentStartTag.containsKey(styleId)){
//				System.out.println("AIPDialog.doStartTag()htContentStartTag.containsKey(styleId)");
//				contentStartTag = (String) htContentStartTag.get(styleId);
//			}
//			if(htContentEndTag.containsKey(styleId)){
//				System.out.println("AIPDialog.doStartTag()htContentEndTag.containsKey(styleId)");
//				contentEndTag = (String) htContentEndTag.get(styleId);
//			}
			
			//System.out.println("AIPDialog.doStartTag():contentStartTag11111111111111111111111111=\n"+contentStartTag);
			
			String currentFolder = baseFolder+"/dialog/"+style+"/";
			loadContentFile(currentFolder+"dialog.html");
			
			pageContext.getOut().write(getContentStartTag());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
////it is immediate load of content in page that isn't useful		
//		if(loader!=null && !"".equals(loader)) {
//			try{
//				System.out.println("AIPDialog.doStartTag():::::::::::loader="+loader);
//				pageContext.include(loader);
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		}
		
		return res;
	}
	public String getContentStartTag() {
		/*
		 * Replacements
		 */
		StringBuffer sb = new StringBuffer(super.getContentStartTag());
		AMSUtililies.replaceAllString(sb,styleIdPlaceHolder,styleId);
		AMSUtililies.replaceAllString(sb,alignPlaceHolder,align);
		
		AMSUtililies.replaceAllString(sb,imgPlaceHolder,("".equals(img))?"":"");
		//String _titleImg =  ;
		AMSUtililies.replaceAllString(sb,titlePlaceHolder,title);
		
		
		
		AMSUtililies.replaceAllString(sb,screenPositionPlaceHolder,screenPosition);
		//System.out.println("AIPDialog.getContentStartTag()@@@@@@@@@@@@@@@@@@@@@@@loader="+loader);
		AMSUtililies.replaceString(sb,loaderPlaceHolder,loader);

		/*
		 * height
		 */
		String heightFull= ( height!=null && !"".equals(height) ) ? "height:"+height : ""; 
		AMSUtililies.replaceAllString(sb,heightPlaceHolder,heightFull);
		/*
		 * isIncludeJQuery
		 */
		//System.out.println("AIPDialog.replacePlaceHolders():isIncludeJQuery="+isIncludeJQuery);
		if( NVL.getBool(isIncludeJQuery)==false) {
			AMSUtililies.replaceString(sb,jqueryPlaceHolder,jqueryContent);
		}else {
			AMSUtililies.replaceString(sb,jqueryPlaceHolder,"");
		}
		
		return sb.toString();
	}
		
		/*
		 * contentEndTag
		 */
		//System.out.println("AIPDialog.replacePlaceHolders():contentEndTag:start");
	public String getContentEndTag() {
		StringBuffer sb = new StringBuffer(super.getContentEndTag());
		AMSUtililies.replaceAllString(sb,styleIdPlaceHolder,styleId);
		
		StringBuffer sbToolbar = new StringBuffer(toolbar);
		AMSUtililies.replaceAllString(sbToolbar,"dialog1",styleId);
		AMSUtililies.replaceAllString(sbToolbar,"[ONOKCLICKPLACEHOLDER]",onOKClick);
		sbToolbar.append(toolbarExtra);
		
		String toolbar=sbToolbar.toString();
		if( "".equals(toolbar) ) {
			AMSUtililies.replaceAllString(sb,"[TOOLBARSTYLE_PLACEHOLDER]","display:none;");
		}else {
			AMSUtililies.replaceAllString(sb,"[TOOLBARSTYLE_PLACEHOLDER]","");
		}
		AMSUtililies.replaceAllString(sb,toolbarPlaceHolder,toolbar);
		
		return sb.toString();
		//System.out.println("AIPDialog.replacePlaceHolders():contentEndTag:end");
	}

	/**
	 * @return the img
	 */
	public String getImg() {
		return img;
	}

	/**
	 * @param img the img to set
	 */
	public void setImg(String img) {
		this.img = img;
	}

	/**
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * @return the screenPosition
	 */
	public String getScreenPosition() {
		return screenPosition;
	}

	/**
	 * @param screenPosition the screenPosition to set
	 */
	public void setScreenPosition(String screenPosition) {
		this.screenPosition = screenPosition;
	}

	/**
	 * @return the loader
	 */
	public String getLoader() {
		return loader;
	}

	/**
	 * @param loader the loader to set
	 */
	public void setLoader(String loader) {
		this.loader = loader;
	}

	/**
	 * @return the isIncludeJQuery
	 */
	public String getIsIncludeJQuery() {
		return isIncludeJQuery;
	}

	/**
	 * @param isIncludeJQuery the isIncludeJQuery to set
	 */
	public void setIsIncludeJQuery(String isIncludeJQuery) {
		this.isIncludeJQuery = isIncludeJQuery;
	}

	/**
	 * @return the toolbar
	 */
	public String getToolbar() {
		return toolbar;
	}

	/**
	 * @param toolbar the toolbar to set
	 */
	public void setToolbar(String toolbar) {
		this.toolbar = toolbar;
	}

	/**
	 * @return the onOKClick
	 */
	public String getOnOKClick() {
		return onOKClick;
	}

	/**
	 * @param onOKClick the onOKClick to set
	 */
	public void setOnOKClick(String onOKClick) {
		this.onOKClick = onOKClick;
	}

	/**
	 * @return the toolbarExtra
	 */
	public String getToolbarExtra() {
		return toolbarExtra;
	}

	/**
	 * @param toolbarExtra the toolbarExtra to set
	 */
	public void setToolbarExtra(String toolbarExtra) {
		this.toolbarExtra = toolbarExtra;
	}




	

}

