/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package struts.actions.tiles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 09-21-2010
 * 
 * XDoclet definition:
 * @struts.action parameter="reqCode" validate="true"
 * @struts.action-forward name="list" path="/jsp/farsi/news/newsList.jsp"
 */
public class T_SecurityAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String reqCode=request.getParameter("reqCode");
		return mapping.findForward(reqCode);
	}
}