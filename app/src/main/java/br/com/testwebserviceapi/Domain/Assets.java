package br.com.testwebserviceapi.Domain;

public class Assets {

    //URLs
    public static final String API_LOGIN = "https://serene-sea-70010.herokuapp.com/login/verify/";
    public static final String API_REGISTER = "https://serene-sea-70010.herokuapp.com/login/create";

    //Mensagens
    public static final String INVALID_DATA = "Email ou senha incorretos";
    public static final String NO_CONNECTION = "Verifique sua conexão com a internet!";
    public static final String INVALID_PASSWD = "Senha inválida";
    public static final String INVALID_EMAIL = "Email inválido";
    public static final String USER_EXISTS = "Usuario já existe";
    public static final String ACCOUNT_CREATED = "Usuario já existe";
    public static final String FATAL_ERROR = "Não foi possivel realizar o cadastro, tente novamente mais tarde ou verifique os dados informados";

    //Msg Logs
    public static final String LOG_ERRO = "LOG ERRO >>> ";
    public static final String LOG_RESPONSE = "RESPOSTA >>> ";
    public static final String LOG = "Log >>> ";
}
