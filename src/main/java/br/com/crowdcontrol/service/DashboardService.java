package br.com.crowdcontrol.service;

import br.com.crowdcontrol.model.OrdemServico;
import br.com.crowdcontrol.model.StatusOs;
import br.com.crowdcontrol.model.StatusUsuario;
import br.com.crowdcontrol.model.Usuario;
import br.com.crowdcontrol.repository.OrdemServicoRepository;
import br.com.crowdcontrol.repository.UsuarioRepository;
import java.util.List;

/**
 * Antes, estes cálculos estavam em TelaPrincipal.carregarDashboard(), misturados
 * com setText() de labels. O método calcular() é estático e puro: recebe listas e
 * devolve números, o que o torna ideal para teste unitário (etapa 7).
 */
public class DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final OrdemServicoRepository ordemRepository;

    public DashboardService(UsuarioRepository usuarioRepository, OrdemServicoRepository ordemRepository) {
        this.usuarioRepository = usuarioRepository;
        this.ordemRepository = ordemRepository;
    }

    public ResumoDashboard gerarResumo() {
        return calcular(usuarioRepository.listar(), ordemRepository.listar());
    }

    public static ResumoDashboard calcular(List<Usuario> usuarios, List<OrdemServico> ordens) {
        int ativos = (int) usuarios.stream()
                .filter(u -> u.getStatus() == StatusUsuario.ATIVO)
                .count();

        return new ResumoDashboard(
            usuarios.size(),
            ativos,
            ordens.size(),
            contar(ordens, StatusOs.PENDENTE),
            contar(ordens, StatusOs.EM_ANDAMENTO),
            contar(ordens, StatusOs.CONCLUIDA)
        );
    }

    private static int contar(List<OrdemServico> ordens, StatusOs status) {
        return (int) ordens.stream().filter(o -> o.getStatus() == status).count();
    }
}
