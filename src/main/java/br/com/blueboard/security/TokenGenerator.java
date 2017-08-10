package br.com.blueboard.security;

import java.util.Base64;
import java.util.Date;

import br.com.blueboard.entity.Student;

public class TokenGenerator {
	public static String generateToken(Student student) {

		String token = "";
		String dateNow = "";
		String numberPart = "";
		String fakePart = "aDSIdugdioDLNJhipJOJOs";
		
		dateNow = new Date().toString();
		dateNow = Base64.getEncoder().encodeToString(dateNow.getBytes());
		dateNow = dateNow + Base64.getEncoder().encodeToString(dateNow.getBytes());
		
		fakePart = Base64.getEncoder().encodeToString(fakePart.getBytes());
		fakePart = Base64.getEncoder().encodeToString(fakePart.getBytes());

		token = numberPart + dateNow + fakePart;
		token = Base64.getEncoder().encodeToString(token.getBytes());
		token += Base64.getEncoder().encodeToString(token.getBytes());

		return token.substring(0, 255);
	}
}
