package br.com.crowdcontrol.web.dto;

import br.com.crowdcontrol.model.OrdemServico;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrdemDto(Integer id, String titulo, String descricao, String prioridade,
                       String status, String responsavel, String prazo) {

    public static OrdemDto de(OrdemServico o) {
        return new OrdemDto(o.getId(), o.getTitulo(), o.getDescricao(),
                Rotulos.prioridade(o.getPrioridade()), Rotulos.statusOs(o.getStatus()),
                o.getAtribuidaPara(), o.getPrazo() != null ? o.getPrazo().toString() : null);
    }
}
