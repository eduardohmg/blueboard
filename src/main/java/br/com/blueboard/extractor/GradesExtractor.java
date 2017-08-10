package br.com.blueboard.extractor;

import java.io.IOException;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.blueboard.dao.GradeDAO;
import br.com.blueboard.dao.ProfessorDAO;
import br.com.blueboard.dao.SubjectDAO;
import br.com.blueboard.entity.Grade;
import br.com.blueboard.entity.Professor;
import br.com.blueboard.entity.Subject;

@Transactional
@Component
public class GradesExtractor {

	private static final String URL_GRADES_SEARCH = "https://aluno.sociesc.com.br/SOL/aluno/index.php/disciplinas/index/buscar";
	private static final String URL_GRADES_MODULE = "https://aluno.sociesc.com.br/SOL/aluno/index.php/disciplinas/dev/mod/notas";
	private LoginMaker loginMaker;

	@Autowired
	private ProfessorDAO professorDAO;

	@Autowired
	private SubjectDAO subjectDAO;
	
	@Autowired
	private GradeDAO gradeDAO;

	public GradesExtractor() {
		loginMaker = new LoginMaker("113000375", "031097");
		try {
			loginMaker.setCookiesAndLogin();
		} catch (IOException e) {
		}
	}

	public void craw() throws IOException, JSONException {
		Document docNotas = setGradesModuleOnCookiesAndGetPage();

		Element el = docNotas.select("#cmbCodPeriodoLetivo").first();
		Elements opts = el.getElementsByTag("option");
		
		for (Element option : opts)
			processOption(option);
	}

	private void processOption(Element option) throws JSONException, IOException {
		String value = option.attr("value");
		if (isNotEmpty(value)) {
			JSONObject jsonPeriodos = new JSONObject(value);
			String periodo = jsonPeriodos.get("SGLPERIODOLETIVO").toString();
			String codPeriodo = jsonPeriodos.get("CODPERIODOLETIVO").toString();

			System.out.println(periodo + " -> " + codPeriodo);

			String rawSubjects = getSubjects(codPeriodo);

			JSONObject subjects = new JSONObject(rawSubjects);
			JSONArray subjectData = subjects.getJSONArray("DADOS");

			processAllSubjectsData(codPeriodo, subjectData);
		}
	}

	private boolean isNotEmpty(String value) {
		return !value.equals(" ");
	}

	private void processAllSubjectsData(String codPeriodo, JSONArray subjectData) throws JSONException, IOException {
		for (int i = 0; i < subjectData.length(); i++) {
			JSONObject disciplina = subjectData.getJSONObject(i);
			processSubject(codPeriodo, disciplina);
		}
	}

	private void processSubject(String codPeriodo, JSONObject disciplina) throws JSONException, IOException {
		Professor professor = extractAndSaveProfessor(disciplina);
		Subject subject = extractAndSaveSubject(disciplina, professor);
		
		System.out.println("   " + subject.getName() + " -> " + subject.getCodSol());

		String rawGrades = getGrades(codPeriodo, subject.getCodSol());
		JSONObject grades = new JSONObject(rawGrades);
		
		String detailGrades = grades.getJSONObject("detalheNotas").get("HTML").toString();

		Element tableGrades = Jsoup.parse(detailGrades);
		processTableGrades(tableGrades, subject);
	}

	private Subject extractAndSaveSubject(JSONObject disciplina, Professor professor) throws JSONException {
		Subject subject = new Subject();
		
		String codDisciplina = disciplina.getString("CODDISCIPLINA");
		String nomeDisciplina = disciplina.getString("NOMDISCIPLINA");
		String className = disciplina.getString("NOMTURMADISCIPLINA");
		String json = disciplina.getString("JSON");
		
		subject.setName(nomeDisciplina);
		subject.setCodSol(codDisciplina);
		subject.setProfessor(professor);
		subject.setJson(json);
		subject.setClassName(className);
		
		subjectDAO.insertIfNotExists(subject);
		
		return subject;
	}

	private Professor extractAndSaveProfessor(JSONObject disciplina) throws JSONException {
		Professor professor = new Professor();

		String codProfessor = disciplina.getString("CODPROFESSOR");
		String nomProfessor = disciplina.getString("NOMPROFESSOR");

		professor.setName(nomProfessor);
		professor.setCodSol(codProfessor);

		professor = professorDAO.insertIfNotExists(professor);
		return professor;
	}

	private void processTableGrades(Element tableNotas, Subject subject) {
		Element tableBody = tableNotas.getElementsByTag("tbody").get(1);
		Elements tableRows = tableBody.getElementsByTag("tr");

		for (Element row : tableRows) {
			Grade grade = new Grade();
			grade.setSubject(subject);
			Elements rowCells = row.getElementsByTag("td");
			System.out.print("      ");
			
			grade.setName(rowCells.get(1).val());
			grade.setTotal(Double.valueOf(rowCells.get(4).val().replaceAll(",", ".")));
			grade.setValue(Double.valueOf(rowCells.get(5).val().replaceAll(",", ".")));
			
			if(!rowCells.get(6).val().isEmpty() && !rowCells.get(6).val().contains("%"))
				grade.setWeight(Double.valueOf(rowCells.get(6).val().replaceAll(",", ".")));
			
			grade.setPercent(Double.valueOf(grade.getValue() / grade.getTotal()));
			
			gradeDAO.insertIfNotExists(grade);
			
			for (int i = 0; i < rowCells.size(); i++) {
				Element cell = rowCells.get(i);
				System.out.print(cell.val() + " | ");
			}
				
		}
	}

	private String getGrades(String codPeriodo, String codDisciplina) throws IOException {
		return Jsoup.connect(URL_GRADES_SEARCH)
				.ignoreContentType(true)
				.cookies(getLoginData().loginCookies)
				.userAgent(LoginData.USER_AGENT)
				.data("__ajax", "1")
				.data("CODPERIODOLETIVO", codPeriodo)
				.data("CODDISCIPLINA", codDisciplina)
				.data("INDBUSCARNOTAS", "S")
				.data("INDDETALHESNOTAS", "S")
				.method(Connection.Method.POST)
				.execute()
				.body();
	}

	private String getSubjects(String codPeriodo) throws IOException {
		return Jsoup.connect(URL_GRADES_SEARCH)
				.ignoreContentType(true)
				.cookies(getLoginData().loginCookies)
				.userAgent(LoginData.USER_AGENT)
				.data("__ajax", "1")
				.data("CODPERIODOLETIVO", codPeriodo)
				.data("INDBUSCARDISCIPLINAS", "S")
				.method(Connection.Method.POST)
				.execute()
				.body();
	}

	private Document setGradesModuleOnCookiesAndGetPage() throws IOException {
		return Jsoup.connect(URL_GRADES_MODULE)
				.cookies(getLoginData().loginCookies)
				.get();
	}

	private LoginData getLoginData() {
		return loginMaker.getLoginData();
	}
}
