package br.com.blueboard.extractor;

import java.util.HashMap;
import java.util.Map;

public class LoginData {
	public static final String COOKIES_URL = "https://aluno.sociesc.com.br/SOL/aluno/index.php/index/seguranca/dev/instituicao/12";
	public static final String LOGIN_URL = "https://aluno.sociesc.com.br/SOL/aluno/index.php/index/seguranca/login";
	public static final String USER_AGENT = "Mozilla/5.0";
	public Map<String, String> loginCookies = new HashMap<String, String>();
	public String user = "";
	public String pass = "";
	
	public LoginData (String user, String pass) {
		this.user = user;
		this.pass = pass;
	}
}
