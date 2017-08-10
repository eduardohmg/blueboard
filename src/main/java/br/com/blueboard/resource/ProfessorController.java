package br.com.blueboard.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.blueboard.dao.ProfessorDAO;
import br.com.blueboard.entity.Professor;

@RestController
@RequestMapping("/professor")
public class ProfessorController {
	
	@Autowired
	private ProfessorDAO professorDAO;

	@RequestMapping(path = {"all","all/"}, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Professor>> list() {
		return new ResponseEntity<List<Professor>>(professorDAO.list(), HttpStatus.OK);
	}
}
