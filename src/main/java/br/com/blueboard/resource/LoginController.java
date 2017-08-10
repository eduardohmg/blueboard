package br.com.blueboard.resource;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.blueboard.annotation.Public;
import br.com.blueboard.dao.StudentDAO;
import br.com.blueboard.entity.Student;
import br.com.blueboard.security.BlueLoginMaker;

@RestController
@CrossOrigin
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private BlueLoginMaker loginMaker;
	
	@Autowired
	private StudentDAO studentDAO;
	
	@Public
	@RequestMapping(path = {"","/"}, method = POST)
	@ResponseBody
	public ResponseEntity<String> login(@RequestBody Student student) {
		String token = loginMaker.loginAndGetToken(student);
		if (token.equals(""))
			return new ResponseEntity<String>("", UNAUTHORIZED);
		else
			return new ResponseEntity<String>(token, OK);
	}
	
	@RequestMapping(path = {"all","all/"}, method = POST)
	@ResponseBody
	public ResponseEntity<List<Student>> list() {
		return new ResponseEntity<List<Student>>(studentDAO.list(), OK);
	}
}
