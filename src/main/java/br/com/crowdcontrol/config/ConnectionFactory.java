package br.com.crowdcontrol.config;

import br.com.crowdcontrol.exception.RepositoryException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Cria conexões JDBC. Agora é uma instância (recebe o DbConfig), e não mais um
 * método estático com dados fixos, então pode ser injetada nos repositórios.
 */
public class ConnectionFactory {

    private final DbConfig config;

    public ConnectionFactory(DbConfig config) {
        this.config = config;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
        } catch (SQLException e) {
            throw new RepositoryException("Erro na conexão com o banco: " + e.getMessage(), e);
        }
    }
}
