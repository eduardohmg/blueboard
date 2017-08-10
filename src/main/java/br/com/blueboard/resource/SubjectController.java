package br.com.blueboard.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.blueboard.dao.SubjectDAO;
import br.com.blueboard.entity.Subject;

@RestController
@RequestMapping("/subject")
public class SubjectController {
	
	@Autowired
	private SubjectDAO SubjectDAO;

	@RequestMapping(path = {"all","all/"}, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Subject>> list() {
		return new ResponseEntity<List<Subject>>(SubjectDAO.list(), HttpStatus.OK);
	}
}
