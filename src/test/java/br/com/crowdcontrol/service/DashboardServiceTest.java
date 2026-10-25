package br.com.crowdcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.crowdcontrol.model.OrdemServico;
import br.com.crowdcontrol.model.Prioridade;
import br.com.crowdcontrol.model.StatusOs;
import br.com.crowdcontrol.model.StatusUsuario;
import br.com.crowdcontrol.model.TipoUsuario;
import br.com.crowdcontrol.model.Usuario;
import br.com.crowdcontrol.repository.memoria.MemoriaOrdemServicoRepository;
import br.com.crowdcontrol.repository.memoria.MemoriaUsuarioRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testa os cálculos do dashboard. São testes de "função pura": entram listas,
 * sai um resumo, sem banco de dados e sem tela.
 */
class DashboardServiceTest {

    private static Usuario usuario(String email, StatusUsuario status) {
        return new Usuario("Nome", email, "123", "Cargo", "Depto", status, "0000", TipoUsuario.FUNCIONARIO);
    }

    private static OrdemServico ordem(String titulo, StatusOs status) {
        return new OrdemServico.Builder(titulo, Prioridade.MEDIA).status(status).build();
    }

    @Test
    @DisplayName("Conta o total de usuários e só os ativos")
    void contaUsuariosAtivos() {
        List<Usuario> usuarios = List.of(
            usuario("a@x.com", StatusUsuario.ATIVO),
            usuario("b@x.com", StatusUsuario.INATIVO),
            usuario("c@x.com", StatusUsuario.ATIVO));

        ResumoDashboard resumo = DashboardService.calcular(usuarios, List.of());

        assertEquals(3, resumo.totalUsuarios());
        assertEquals(2, resumo.usuariosAtivos());
    }

    @Test
    @DisplayName("Conta as ordens de serviço por status")
    void contaOrdensPorStatus() {
        List<OrdemServico> ordens = List.of(
            ordem("A", StatusOs.PENDENTE),
            ordem("B", StatusOs.PENDENTE),
            ordem("C", StatusOs.EM_ANDAMENTO),
            ordem("D", StatusOs.CONCLUIDA),
            ordem("E", StatusOs.FINALIZADA));

        ResumoDashboard resumo = DashboardService.calcular(List.of(), ordens);

        assertEquals(5, resumo.totalOrdens());
        assertEquals(2, resumo.ordensPendentes());
        assertEquals(1, resumo.ordensEmAndamento());
        assertEquals(1, resumo.ordensConcluidas());
    }

    @Test
    @DisplayName("Listas vazias geram tudo zerado e sem pendências")
    void listasVazias() {
        ResumoDashboard resumo = DashboardService.calcular(List.of(), List.of());

        assertEquals(0, resumo.totalUsuarios());
        assertEquals(0, resumo.totalOrdens());
        assertFalse(resumo.temPendencias());
    }

    @Test
    @DisplayName("Mensagem de pendências mostra a quantidade quando há ordens pendentes")
    void mensagemComPendencias() {
        ResumoDashboard resumo = DashboardService.calcular(
            List.of(), List.of(ordem("A", StatusOs.PENDENTE), ordem("B", StatusOs.PENDENTE)));

        assertTrue(resumo.temPendencias());
        assertEquals("Você tem 2 ordem(ns) de serviço pendente(s)", resumo.mensagemPendencias());
        assertEquals("Atribua essas ordens aos funcionários para iniciar o trabalho",
            resumo.orientacaoPendencias());
    }

    @Test
    @DisplayName("Mensagem de sucesso quando não há ordens pendentes")
    void mensagemSemPendencias() {
        ResumoDashboard resumo = DashboardService.calcular(
            List.of(), List.of(ordem("A", StatusOs.CONCLUIDA)));

        assertFalse(resumo.temPendencias());
        assertEquals("Nenhuma ordem pendente", resumo.mensagemPendencias());
        assertEquals("Sistema operando normalmente, ótimo trabalho!", resumo.orientacaoPendencias());
    }

    @Test
    @DisplayName("gerarResumo() lê os dados dos repositórios")
    void gerarResumoUsaRepositorios() {
        MemoriaUsuarioRepository usuarios = new MemoriaUsuarioRepository();
        usuarios.salvar(usuario("a@x.com", StatusUsuario.ATIVO));
        MemoriaOrdemServicoRepository ordens = new MemoriaOrdemServicoRepository();
        ordens.salvar(ordem("A", StatusOs.EM_ANDAMENTO));

        ResumoDashboard resumo = new DashboardService(usuarios, ordens).gerarResumo();

        assertEquals(1, resumo.usuariosAtivos());
        assertEquals(1, resumo.ordensEmAndamento());
    }
}
