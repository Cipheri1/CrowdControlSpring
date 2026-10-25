package br.com.crowdcontrol.web.controller;

import br.com.crowdcontrol.exception.RegraNegocioException;
import br.com.crowdcontrol.model.Chamado;
import br.com.crowdcontrol.model.Prioridade;
import br.com.crowdcontrol.model.StatusChamado;
import br.com.crowdcontrol.service.ChamadoService;
import br.com.crowdcontrol.web.dto.ChamadoDto;
import br.com.crowdcontrol.web.dto.Rotulos;
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
@RequestMapping("/api/chamados")
public class ChamadoController {

    private final ChamadoService service;

    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ChamadoDto> listar(@RequestParam(required = false) String status) {
        List<Chamado> chamados = (status == null || status.isBlank())
                ? service.listar()
                : service.listarPorStatus(Rotulos.paraEnum(StatusChamado.class, status));
        return chamados.stream().map(ChamadoDto::de).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChamadoDto criar(@RequestBody ChamadoDto dados) {
        Chamado chamado = new Chamado(dados.titulo(), dados.descricao(), dados.solicitante(), dados.categoria(),
                Rotulos.paraEnum(Prioridade.class, dados.prioridade()),
                Rotulos.paraEnum(StatusChamado.class, dados.status()));
        service.criar(chamado);
        return ChamadoDto.de(chamado);
    }

    @PutMapping("/{id}")
    public ChamadoDto atualizar(@PathVariable int id, @RequestBody ChamadoDto dados) {
        Chamado chamado = service.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Chamado não encontrado!"));
        chamado.setTitulo(dados.titulo());
        chamado.setDescricao(dados.descricao());
        chamado.setSolicitante(dados.solicitante());
        chamado.setCategoria(dados.categoria());
        chamado.setPrioridade(Rotulos.paraEnum(Prioridade.class, dados.prioridade()));
        chamado.setStatus(Rotulos.paraEnum(StatusChamado.class, dados.status()));
        service.atualizar(chamado);
        return ChamadoDto.de(chamado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable int id) {
        service.excluir(id);
    }
}
