package br.com.blueboard.extractor;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import br.com.blueboard.entity.Student;

public class SolLoginMaker {
	private LoginData loginData;

	public SolLoginMaker(String user, String pass) {
		loginData = new LoginData(user, pass);
	}
	
	public SolLoginMaker(Student student) {
		loginData = new LoginData(student.getNumber(), student.getPassword());
	}
	
	public Boolean login() {
		try {
			Response response = setCookiesAndLogin();
			if(!response.body().equals("{\"restricao\":\"\"}"))
				return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public Response setCookiesAndLogin() throws IOException {
		this.setSessionCookies();
		Connection connection = Jsoup.connect(LoginData.LOGIN_URL);
		return connection.userAgent(LoginData.USER_AGENT)
				.cookies(loginData.loginCookies)
				.ignoreContentType(true)
				.data("__ajax", "1")
				.data("undefined", "Aluno")
				.data("opt_cpf", "Por CPF")
				.data("instituicao", "12")
				.data("logar", "Entrar")
				.data("opcao_acesso", "1")
				.data("comboAcesso", "{\"CONDICAO\":\"1\",\"NOMFILTRO\":\"Aluno\"}")
				.data("matricula", loginData.user)
				.data("senha", loginData.pass)
				.method(Connection.Method.POST)
				.execute();
	}

	private void setSessionCookies() throws IOException {
		loginData.loginCookies = Jsoup.connect(LoginData.COOKIES_URL)
				.method(Connection.Method.GET)
				.execute()
				.cookies();
	}
	
	public LoginData getLoginData() {
		return loginData;
	}
}
