package br.com.crowdcontrol.config;

import br.com.crowdcontrol.exception.RepositoryException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lê URL, usuário e senha do banco. Antes isso ficava escrito dentro de
 * ConnectionFactory (inclusive a senha). Ordem de busca:
 *   1) variáveis de ambiente DB_URL, DB_USER, DB_PASSWORD
 *   2) arquivo db.properties no classpath (src/main/resources)
 */
public class DbConfig {

    private final String url;
    private final String user;
    private final String password;

    public DbConfig(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static DbConfig carregar() {
        Properties props = new Properties();
        try (InputStream in = DbConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new RepositoryException("Não foi possível ler db.properties", e);
        }

        String url = valor("DB_URL", props.getProperty("db.url"));
        String user = valor("DB_USER", props.getProperty("db.user"));
        String password = valor("DB_PASSWORD", props.getProperty("db.password"));

        if (url == null || user == null) {
            throw new RepositoryException(
                "Configuração do banco ausente. Crie src/main/resources/db.properties "
                + "(veja db.properties.example) ou defina DB_URL, DB_USER e DB_PASSWORD.", null);
        }
        return new DbConfig(url, user, password == null ? "" : password);
    }

    private static String valor(String variavelAmbiente, String padrao) {
        String doAmbiente = System.getenv(variavelAmbiente);
        return (doAmbiente != null && !doAmbiente.isBlank()) ? doAmbiente : padrao;
    }

    public String getUrl() { return url; }
    public String getUser() { return user; }
    public String getPassword() { return password; }
}
