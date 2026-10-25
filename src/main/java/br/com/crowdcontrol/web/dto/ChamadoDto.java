package br.com.crowdcontrol.web.dto;

import br.com.crowdcontrol.model.Chamado;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChamadoDto(Integer id, String titulo, String descricao, String solicitante,
                         String categoria, String prioridade, String status) {

    public static ChamadoDto de(Chamado c) {
        return new ChamadoDto(c.getId(), c.getTitulo(), c.getDescricao(), c.getSolicitante(),
                c.getCategoria(), Rotulos.prioridade(c.getPrioridade()), Rotulos.statusChamado(c.getStatus()));
    }
}
