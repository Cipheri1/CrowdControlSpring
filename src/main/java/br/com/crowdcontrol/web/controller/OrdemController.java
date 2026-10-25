package br.com.crowdcontrol.web.controller;

import br.com.crowdcontrol.exception.RegraNegocioException;
import br.com.crowdcontrol.model.OrdemServico;
import br.com.crowdcontrol.model.Prioridade;
import br.com.crowdcontrol.model.StatusOs;
import br.com.crowdcontrol.service.OrdemServicoService;
import br.com.crowdcontrol.web.config.AuthInterceptor;
import br.com.crowdcontrol.web.dto.OrdemDto;
import br.com.crowdcontrol.web.dto.Rotulos;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ordens")
public class OrdemController {

    private final OrdemServicoService service;

    public OrdemController(OrdemServicoService service) {
        this.service = service;
    }

    /** GET /api/ordens ou GET /api/ordens?status=Em andamento */
    @GetMapping
    public List<OrdemDto> listar(@RequestParam(required = false) String status) {
        List<OrdemServico> ordens = (status == null || status.isBlank())
                ? service.listarOrdens()
                : service.listarPorStatus(Rotulos.paraEnum(StatusOs.class, status));
        return ordens.stream().map(OrdemDto::de).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemDto criar(@RequestBody OrdemDto dados, HttpSession sessao) {
        OrdemServico ordem = new OrdemServico.Builder(dados.titulo(), Rotulos.paraEnum(Prioridade.class, dados.prioridade()))
                .descricao(dados.descricao())
                .status(Rotulos.paraEnum(StatusOs.class, dados.status()))
                .prazo(Rotulos.data(dados.prazo()))
                .atribuidaPara(Rotulos.vazioParaNulo(dados.responsavel()))
                .solicitante((String) sessao.getAttribute(AuthInterceptor.SESSAO_USUARIO)) // quem está logado
                .build();
        service.criarOrdem(ordem);
        return OrdemDto.de(ordem);
    }

    @PutMapping("/{id}")
    public OrdemDto atualizar(@PathVariable int id, @RequestBody OrdemDto dados) {
        OrdemServico ordem = service.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Ordem não encontrada!"));
        ordem.setTitulo(dados.titulo());
        ordem.setDescricao(dados.descricao());
        ordem.setPrioridade(Rotulos.paraEnum(Prioridade.class, dados.prioridade()));
        ordem.setStatus(Rotulos.paraEnum(StatusOs.class, dados.status()));
        ordem.setPrazo(Rotulos.data(dados.prazo()));
        ordem.setAtribuidaPara(Rotulos.vazioParaNulo(dados.responsavel()));
        service.atualizarOrdem(ordem);
        return OrdemDto.de(ordem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable int id) {
        service.excluirOrdem(id);
    }
}
