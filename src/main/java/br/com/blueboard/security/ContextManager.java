package br.com.blueboard.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import br.com.blueboard.entity.Student;

@Component
@Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ContextManager {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private Student student;

	public void setLoggedStudent(Student student) {
		this.student = student;
	}
	
	public Student getLoggedStudent() {
		return this.student;
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
