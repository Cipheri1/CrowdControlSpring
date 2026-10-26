package br.com.crowdcontrol.web.config;

import br.com.crowdcontrol.config.ConnectionFactory;
import br.com.crowdcontrol.config.DbConfig;
import br.com.crowdcontrol.repository.ChamadoRepository;
import br.com.crowdcontrol.repository.OrdemServicoRepository;
import br.com.crowdcontrol.repository.UsuarioRepository;
import br.com.crowdcontrol.repository.jdbc.JdbcChamadoRepository;
import br.com.crowdcontrol.repository.jdbc.JdbcOrdemServicoRepository;
import br.com.crowdcontrol.repository.jdbc.JdbcUsuarioRepository;
import br.com.crowdcontrol.service.AutenticacaoService;
import br.com.crowdcontrol.service.ChamadoService;
import br.com.crowdcontrol.service.DashboardService;
import br.com.crowdcontrol.service.OrdemServicoService;
import br.com.crowdcontrol.service.UsuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Monta os objetos do núcleo da etapa 6 e os entrega ao Spring.
 * As classes do núcleo continuam sem nenhuma anotação do Spring.
 */
@Configuration
public class AppConfig {

    @Bean
    ConnectionFactory connectionFactory(@Value("${crowdcontrol.db.url}") String url,
                                        @Value("${crowdcontrol.db.user}") String user,
                                        @Value("${crowdcontrol.db.password}") String password) {
        return new ConnectionFactory(new DbConfig(url, user, password));
    }

    @Bean
    UsuarioRepository usuarioRepository(ConnectionFactory fabrica) {
        return new JdbcUsuarioRepository(fabrica);
    }

    @Bean
    OrdemServicoRepository ordemServicoRepository(ConnectionFactory fabrica) {
        return new JdbcOrdemServicoRepository(fabrica);
    }

    @Bean
    ChamadoRepository chamadoRepository(ConnectionFactory fabrica) {
        return new JdbcChamadoRepository(fabrica);
    }

    @Bean
    UsuarioService usuarioService(UsuarioRepository repositorio) {
        return new UsuarioService(repositorio);
    }

    @Bean
    OrdemServicoService ordemServicoService(OrdemServicoRepository repositorio) {
        return new OrdemServicoService(repositorio);
    }

    @Bean
    ChamadoService chamadoService(ChamadoRepository repositorio) {
        return new ChamadoService(repositorio);
    }

    @Bean
    AutenticacaoService autenticacaoService(UsuarioRepository repositorio) {
        return new AutenticacaoService(repositorio);
    }

    @Bean
    DashboardService dashboardService(UsuarioRepository usuarios, OrdemServicoRepository ordens) {
        return new DashboardService(usuarios, ordens);
    }
}
