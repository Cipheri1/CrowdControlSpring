package br.com.crowdcontrol.repository.jdbc;

import br.com.crowdcontrol.config.ConnectionFactory;
import br.com.crowdcontrol.exception.RepositoryException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Concentra o try-with-resources e o tratamento de erro que estavam repetidos
 * em todos os métodos dos três repositories antigos.
 */
public class JdbcHelper {

    @FunctionalInterface
    public interface Preparador {
        void preparar(PreparedStatement stmt) throws SQLException;
    }

    private static final Preparador SEM_PARAMETROS = stmt -> { };

    private final ConnectionFactory connectionFactory;

    public JdbcHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /** INSERT: devolve o id gerado pelo banco. */
    public int inserir(String sql, Preparador preparador) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparador.preparar(stmt);
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                return chaves.next() ? chaves.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao inserir no banco: " + e.getMessage(), e);
        }
    }

    /** UPDATE / DELETE. */
    public void executar(String sql, Preparador preparador) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preparador.preparar(stmt);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException("Erro ao executar comando no banco: " + e.getMessage(), e);
        }
    }

    public <T> List<T> consultarLista(String sql, Preparador preparador, RowMapper<T> mapper) {
        List<T> resultado = new ArrayList<>();

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preparador.preparar(stmt);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapper.mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Erro ao consultar o banco: " + e.getMessage(), e);
        }
        return resultado;
    }

    public <T> List<T> consultarLista(String sql, RowMapper<T> mapper) {
        return consultarLista(sql, SEM_PARAMETROS, mapper);
    }

    public <T> Optional<T> consultarUm(String sql, Preparador preparador, RowMapper<T> mapper) {
        List<T> lista = consultarLista(sql, preparador, mapper);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }
}
