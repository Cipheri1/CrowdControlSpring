package br.com.crowdcontrol.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.crowdcontrol.model.OrdemServico;
import br.com.crowdcontrol.model.Prioridade;
import br.com.crowdcontrol.model.StatusOs;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FormatadorOrdemTest {

    @Test
    @DisplayName("Descrição maior que o limite é cortada e recebe '...'")
    void descricaoLongaECortada() {
        String descricao = "x".repeat(60);

        assertEquals("x".repeat(50) + "...", FormatadorOrdem.resumirDescricao(descricao, 50));
    }

    @Test
    @DisplayName("Descrição com exatamente o limite não é cortada")
    void descricaoNoLimiteNaoECortada() {
        String descricao = "x".repeat(50);

        assertEquals(descricao, FormatadorOrdem.resumirDescricao(descricao, 50));
    }

    @Test
    @DisplayName("Descrição curta não é alterada")
    void descricaoCurta() {
        assertEquals("curta", FormatadorOrdem.resumirDescricao("curta", 50));
    }

    @Test
    @DisplayName("Descrição nula vira texto vazio")
    void descricaoNula() {
        assertEquals("", FormatadorOrdem.resumirDescricao(null, 50));
    }

    @Test
    @DisplayName("Prazo é formatado como dd/MM/yyyy")
    void prazoFormatado() {
        assertEquals("05/03/2026", FormatadorOrdem.formatarPrazo(LocalDate.of(2026, 3, 5)));
    }

    @Test
    @DisplayName("Prazo nulo vira 'Sem prazo'")
    void prazoNulo() {
        assertEquals("Sem prazo", FormatadorOrdem.formatarPrazo(null));
    }

    @Test
    @DisplayName("Valor nulo vira 'N/A' e valor preenchido é mantido")
    void valorOuNaoInformado() {
        assertEquals("N/A", FormatadorOrdem.valorOuNaoInformado(null));
        assertEquals("Carlos", FormatadorOrdem.valorOuNaoInformado("Carlos"));
    }

    @Test
    @DisplayName("Cabeçalho do card junta id, status e prioridade")
    void cabecalho() {
        OrdemServico os = new OrdemServico.Builder("Teste", Prioridade.ALTA)
                .id(7).status(StatusOs.EM_ANDAMENTO).build();

        assertEquals("OS-7 | EM ANDAMENTO | ALTA", FormatadorOrdem.formatarCabecalho(os));
    }
}
