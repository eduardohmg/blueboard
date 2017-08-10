package br.com.blueboard.resource;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.blueboard.dao.GradeDAO;
import br.com.blueboard.entity.Grade;
import br.com.blueboard.extractor.GradesExtractor;

@RestController
@CrossOrigin
@RequestMapping("/grade")
public class GradeController {
	
	@Autowired
	private GradeDAO GradeDAO;
	
	@Autowired
	private GradesExtractor ss;

	@RequestMapping(path = {"all","all/"}, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Grade>> list() {
		return new ResponseEntity<List<Grade>>(GradeDAO.list(), HttpStatus.OK);
	}
	
	@RequestMapping(path = {"","/"}, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> get() {
		try {
			ss.craw();
		} catch (IOException | JSONException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
}
