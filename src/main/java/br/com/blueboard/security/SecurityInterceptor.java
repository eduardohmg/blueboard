package br.com.blueboard.security;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import br.com.blueboard.annotation.Public;
import br.com.blueboard.dao.StudentDAO;
import br.com.blueboard.entity.Student;

@Component
@Transactional(noRollbackFor = Exception.class)
public class SecurityInterceptor implements HandlerInterceptor {
	
	private static final int LIMIT_AFK_TIME = 600000;

	@Autowired
	private StudentDAO studentDAO;
	
	@Autowired
	private ContextManager contextManager;

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Boolean isAllowed = false;
		try {
			String token = request.getHeader("X-Auth");
			Method method = ((HandlerMethod)handler).getMethod();

			if (!method.isAnnotationPresent(Public.class)) {
				Student student = studentDAO.findByToken(token);
				
				if(student != null) {
					if(new Date().getTime() - student.getLastRequestTime().getTime() < LIMIT_AFK_TIME) { // 10 minutos
						if(student.getLastIP() == null || student.getLastIP().equals(request.getRemoteAddr())) {
							isAllowed = true;
							student.setLastRequestTime(new Date());
							student.setLastIP(request.getRemoteAddr());
							studentDAO.save(student);
							contextManager.setLoggedStudent(student);
						}
					}
				}
			}
			else
				isAllowed = true;
		} catch (Exception ex) {
			isAllowed = false;
		}
		
		if(!isAllowed)
			response.sendError(401);
		return isAllowed;
	}

}
