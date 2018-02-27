package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class AMSPagination extends BodyTagSupport {

	static String contentPattern = "[PLACEHOLDER]";

	int totalRows;
	int currentPage;
	int pageSize;
	String align;
	
	public AMSPagination() {
		super();
	}
	
	public int doStartTag() throws JspException {
		int res = super.doStartTag();
		try {
			String out = loadPagination(true);
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
			throw new JspException("error in AIPPagination: " + ex);
		}
		return super.doAfterBody();
	}

	public int doEndTag() throws JspException {
		int i = super.doEndTag();
		try {
			String out = loadPagination(false);
			out = out.substring(out.indexOf(contentPattern) + contentPattern.length());
			pageContext.getOut().write(out);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return i;
	}
	
	String loadPagination(boolean b) {
		String sb = "";
		try {
			String realPath = pageContext.getServletContext().getRealPath("images/pagination/aip_pagination.html");
			File f = new File(realPath);
			FileInputStream fin = new FileInputStream(f);
			byte buf[] = new byte[fin.available()];
			fin.read(buf);
			fin.close();
			sb = new String(buf);
			String pagination = createPagination(totalRows, currentPage, pageSize);
			if (b) {
				String jq = "<script src=\"images/pagination/aip_pagination.js\" type=\"text/javascript\"></script>";
				jq += "<script src=\"js/common/jquery.getUrlParam.js\" type=\"text/javascript\" ></script>";
				sb = AMSUtililies.replace(sb, "[JQUERY]", jq);
			} else {
				sb = AMSUtililies.replace(sb, "[JQUERY]", "");
			}
			
			if (totalRows > pageSize) {
				if (!b) { 
					pagination += "<input type='hidden' id='totalPages' value='" + ((totalRows / pageSize) + 1) + "'>";
					String  goToPageContent = ((totalRows / pageSize) + 1) + " / <input id='specifiedPage' type='text' onkeyup='validate();' size='3'>";
					goToPageContent += "<input type='button' onclick='goToSpecifiedPage();' id='goToSpecifiedPageBtn' value='>' disabled='disabled' title='برو به ص�?حه'>"; 
					sb = AMSUtililies.replace(sb, "[GOTOPAGECONTENT]", goToPageContent);
				}
			} else {
				sb = AMSUtililies.replace(sb, "[GOTOPAGECONTENT]", "");
			}
			
			
			sb = AMSUtililies.replace(sb, "[CONTENT]", pagination);
			sb = AMSUtililies.replace(sb, "[ALIGN]", getAlign());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int pos = sb.indexOf(contentPattern);
		return sb;
	}
	
	public String createPagination(int totalRows,int currentPage,int pageSize) {
		String str  = "";
		if (totalRows != 0) {
			int count = totalRows / pageSize;
			if (totalRows % pageSize != 0) {
				count += 1;
			}
			if (count > 1) {
				if (currentPage != 1) {
					str+="<a href='#' id='hrefed1' dir='ltr'><img src='images/pagination/first.png'></a>&nbsp;";
					str+="<a href='#' id='hrefed" + (currentPage - 1) + "' dir='ltr'><img src='images/pagination/next.png'></a>&nbsp;";
				} else {
					str+="<a href='#' class='noHref' dir='ltr'><img src='images/pagination/firstD.png'></a>&nbsp;";
					str+="<a href='#' class='noHref' dir='ltr'><img src='images/pagination/nextD.png'></a>&nbsp;";
				}
				if (currentPage < 5) {
					for (int i = 1; i < 10 &&  i <= count; i++) {
						if (currentPage != i) {
							str+="<a href='#' dir='ltr' class='hrefed' id='hrefed" + i + "'> " + i + " </a>";
						} else {
							str+="<a  href='#' dir='ltr' class='noHref'> " + i + " </a>";
						}
					}
				} else if (currentPage > count - 4) {
					for (int j = count - 8; j <= count; j++) {
						if (j > 0) {
							if (currentPage != j) {
								str+="<a href='#' dir='ltr' class='hrefed' id='hrefed" + j + "'> " + j + " </a>";
							} else {
								str+="<a  href='#' dir='ltr' class='noHref'> " + j + " </a>";
							}
						}
					}
				} else {
					for (int i = currentPage - 4; i < currentPage + 5 && i <= count; i++) {
						if (currentPage != i) {
							str+="<a href='#' dir='ltr' class='hrefed' id='hrefed" + i + "'> " + i + " </a>";
						} else {
							str+="<a  href='#' dir='ltr' class='noHref'> " + i + " </a>";
						}
					}
				}
				if (count != 1 && currentPage != count ) {
					str+="&nbsp; <a href='#' id='hrefed" + (currentPage + 1) + "' dir='ltr'><img src='images/pagination/previous.png'></a>";
					str+="&nbsp; <a href='#' id='hrefed" + count + "' dir='ltr'><img src='images/pagination/last.png'></a>";
				} else {
					str+="&nbsp;<a href='#' class='noHref' dir='ltr'><img src='images/pagination/previousD.png'></a>";
					str+="&nbsp;<a href='#' class='noHref' dir='ltr'><img src='images/pagination/lastD.png'></a>";
				}
			}
		}
		return str;
	}
	

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}