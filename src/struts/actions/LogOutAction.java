/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package struts.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LogOutAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) {
		//response.setCharacterEncoding("utf8");
//		if("logOut".equalsIgnoreCase(request.getParameter("reqCode"))){
			request.getSession().invalidate();
//		}
		return new ActionForward("logout","t_location.do?reqCode=pathManagement",true);
	}
}