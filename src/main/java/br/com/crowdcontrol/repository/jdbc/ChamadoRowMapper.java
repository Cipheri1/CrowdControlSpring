package br.com.crowdcontrol.repository.jdbc;

import br.com.crowdcontrol.model.Chamado;
import br.com.crowdcontrol.model.Prioridade;
import br.com.crowdcontrol.model.StatusChamado;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ChamadoRowMapper implements RowMapper<Chamado> {

    @Override
    public Chamado mapear(ResultSet rs) throws SQLException {
        Chamado c = new Chamado(
            rs.getString("titulo"),
            rs.getString("descricao"),
            rs.getString("solicitante"),
            rs.getString("categoria"),
            Prioridade.deBanco(rs.getString("prioridade")),
            StatusChamado.valueOf(rs.getString("status"))
        );
        c.setId(rs.getInt("id"));
        c.setAtribuidaPara(rs.getString("atribuida_para"));

        // Correção: o código antigo ignorava a coluna e o chamado "nascia de novo" a cada leitura.
        Timestamp abertura = rs.getTimestamp("data_abertura");
        if (abertura != null) {
            c.setDataAbertura(abertura.toLocalDateTime());
        }
        return c;
    }
}
