package br.com.crowdcontrol.repository.jdbc;

import br.com.crowdcontrol.model.OrdemServico;
import br.com.crowdcontrol.model.Prioridade;
import br.com.crowdcontrol.model.StatusOs;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdemServicoRowMapper implements RowMapper<OrdemServico> {

    @Override
    public OrdemServico mapear(ResultSet rs) throws SQLException {
        Date prazo = rs.getDate("prazo");

        return new OrdemServico.Builder(rs.getString("titulo"), Prioridade.deBanco(rs.getString("prioridade")))
            .id(rs.getInt("id"))
            .descricao(rs.getString("descricao"))
            .status(StatusOs.valueOf(rs.getString("status")))
            .prazo(prazo != null ? prazo.toLocalDate() : null)
            .solicitante(rs.getString("solicitante"))
            .categoria(rs.getString("categoria"))
            .atribuidaPara(rs.getString("atribuida_para"))
            .local(rs.getString("local"))
            .build();
    }
}
