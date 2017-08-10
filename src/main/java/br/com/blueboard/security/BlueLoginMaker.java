package br.com.blueboard.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.blueboard.dao.StudentDAO;
import br.com.blueboard.entity.Student;
import br.com.blueboard.extractor.SolLoginMaker;

@Component
public class BlueLoginMaker {
	
	@Autowired
	private StudentDAO studentDAO;
	
	public BlueLoginMaker() {
	}
	
	public String loginAndGetToken(Student studentToLogin) {
		String token;
		Student studentFound;
		
		if(!new SolLoginMaker(studentToLogin).login())
			return "";
		else {
			studentFound = studentDAO.findByLogin(studentToLogin.getNumber());
			if(studentFound == null)
				studentFound = studentToLogin;
			else if(!studentFound.getPassword().equals(studentToLogin.getPassword()))
				studentFound.setPassword(studentToLogin.getPassword());
		}
		
		token = TokenGenerator.generateToken(studentFound);
		studentFound.setToken(token);
		studentFound.setLastRequestTime(new Date());
		
		studentDAO.save(studentFound);
		
		return token;
	}
}
