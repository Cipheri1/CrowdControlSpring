package br.com.crowdcontrol.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/** Transforma a linha atual de um ResultSet em um objeto. Escrito uma vez por entidade. */
@FunctionalInterface
public interface RowMapper<T> {
    T mapear(ResultSet rs) throws SQLException;
}
