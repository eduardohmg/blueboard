package br.com.blueboard.extractor;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class LoginMaker {
	private LoginData loginData;

	public LoginMaker(String user, String pass) {
		loginData = new LoginData(user, pass);
	}
	
	public void setCookiesAndLogin() throws IOException {
		this.setSessionCookies();
		Connection connection = Jsoup.connect(LoginData.LOGIN_URL);
		connection.userAgent(LoginData.USER_AGENT)
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
