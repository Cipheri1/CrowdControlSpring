package br.com.crowdcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.crowdcontrol.exception.RegraNegocioException;
import br.com.crowdcontrol.model.OrdemServico;
import br.com.crowdcontrol.model.Prioridade;
import br.com.crowdcontrol.model.StatusOs;
import br.com.crowdcontrol.repository.memoria.MemoriaOrdemServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrdemServicoServiceTest {

    private OrdemServicoService service;

    @BeforeEach
    void preparar() {
        service = new OrdemServicoService(new MemoriaOrdemServicoRepository());
    }

    @Test
    @DisplayName("Builder usa PENDENTE como status padrão")
    void builderStatusPadrao() {
        OrdemServico os = new OrdemServico.Builder("Trocar lâmpada", Prioridade.BAIXA).build();

        assertEquals(StatusOs.PENDENTE, os.getStatus());
    }

    @Test
    @DisplayName("Criar ordens gera ids diferentes")
    void criaOrdensComIdsDiferentes() {
        OrdemServico a = new OrdemServico.Builder("A", Prioridade.BAIXA).build();
        OrdemServico b = new OrdemServico.Builder("B", Prioridade.ALTA).build();

        service.criarOrdem(a);
        service.criarOrdem(b);

        assertTrue(a.getId() > 0);
        assertNotEquals(a.getId(), b.getId());
    }

    @Test
    @DisplayName("Rejeita ordem sem título")
    void rejeitaSemTitulo() {
        assertThrows(RegraNegocioException.class,
            () -> service.criarOrdem(new OrdemServico.Builder("  ", Prioridade.MEDIA).build()));
    }

    @Test
    @DisplayName("Rejeita ordem nula")
    void rejeitaOrdemNula() {
        assertThrows(RegraNegocioException.class, () -> service.criarOrdem(null));
    }

    @Test
    @DisplayName("Filtra as ordens por status")
    void filtraPorStatus() {
        service.criarOrdem(new OrdemServico.Builder("A", Prioridade.BAIXA).build());
        service.criarOrdem(new OrdemServico.Builder("B", Prioridade.ALTA)
                .status(StatusOs.EM_ANDAMENTO).build());

        assertEquals(1, service.listarPorStatus(StatusOs.EM_ANDAMENTO).size());
        assertEquals(1, service.listarPorStatus(StatusOs.PENDENTE).size());
        assertEquals(0, service.listarPorStatus(StatusOs.CONCLUIDA).size());
    }

    @Test
    @DisplayName("Rejeita filtro com status nulo")
    void rejeitaFiltroNulo() {
        assertThrows(RegraNegocioException.class, () -> service.listarPorStatus(null));
    }

    @Test
    @DisplayName("Atualiza a ordem pelo id")
    void atualizaPorId() {
        OrdemServico os = new OrdemServico.Builder("A", Prioridade.BAIXA).build();
        service.criarOrdem(os);

        os.setStatus(StatusOs.CONCLUIDA);
        service.atualizarOrdem(os);

        assertEquals(StatusOs.CONCLUIDA, service.buscarPorId(os.getId()).get().getStatus());
    }

    @Test
    @DisplayName("Rejeita atualizar ordem que não existe")
    void rejeitaAtualizarInexistente() {
        OrdemServico os = new OrdemServico.Builder("A", Prioridade.BAIXA).id(99).build();

        assertThrows(RegraNegocioException.class, () -> service.atualizarOrdem(os));
    }

    @Test
    @DisplayName("Exclui só a ordem do id informado")
    void excluiPorId() {
        OrdemServico a = new OrdemServico.Builder("Igual", Prioridade.BAIXA).build();
        OrdemServico b = new OrdemServico.Builder("Igual", Prioridade.BAIXA).build();
        service.criarOrdem(a);
        service.criarOrdem(b);

        service.excluirOrdem(a.getId());

        assertEquals(1, service.listarOrdens().size());
        assertEquals(b.getId(), service.listarOrdens().get(0).getId());
    }

    @Test
    @DisplayName("Rejeita id inválido (zero ou negativo)")
    void rejeitaIdInvalido() {
        assertThrows(RegraNegocioException.class, () -> service.excluirOrdem(0));
        assertThrows(RegraNegocioException.class, () -> service.buscarPorId(-1));
    }
}
