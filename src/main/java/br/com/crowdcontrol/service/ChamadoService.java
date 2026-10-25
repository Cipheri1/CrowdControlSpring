package br.com.crowdcontrol.service;

import br.com.crowdcontrol.exception.RegraNegocioException;
import br.com.crowdcontrol.model.Chamado;
import br.com.crowdcontrol.model.StatusChamado;
import br.com.crowdcontrol.repository.ChamadoRepository;
import br.com.crowdcontrol.util.Texto;
import java.util.List;
import java.util.Optional;

public class ChamadoService {

    private final ChamadoRepository repository;

    public ChamadoService(ChamadoRepository repository) {
        this.repository = repository;
    }

    public void criar(Chamado chamado) {
        validar(chamado);
        repository.salvar(chamado);
    }

    public List<Chamado> listar() {
        return repository.listar();
    }

    /** Antes o filtro "Resolvidos" era um regexFilter na tabela da tela; agora é regra do service. */
    public List<Chamado> listarPorStatus(StatusChamado status) {
        if (status == null) {
            throw new RegraNegocioException("Status não pode ser nulo");
        }
        return repository.listarPorStatus(status);
    }

    public Optional<Chamado> buscarPorId(int id) {
        validarId(id);
        return repository.buscarPorId(id);
    }

    public void atualizar(Chamado chamado) {
        validar(chamado);
        validarId(chamado.getId());

        if (repository.buscarPorId(chamado.getId()).isEmpty()) {
            throw new RegraNegocioException("Chamado não encontrado!");
        }
        repository.atualizar(chamado);
    }

    public void excluir(int id) {
        validarId(id);
        repository.removerPorId(id);
    }

    private void validar(Chamado c) {
        if (c == null) {
            throw new RegraNegocioException("Chamado não pode ser nulo");
        }
        if (Texto.estaVazio(c.getTitulo())) {
            throw new RegraNegocioException("Título é obrigatório");
        }
        if (c.getPrioridade() == null || c.getStatus() == null) {
            throw new RegraNegocioException("Prioridade e status são obrigatórios");
        }
    }

    private void validarId(int id) {
        if (id <= 0) {
            throw new RegraNegocioException("ID inválido");
        }
    }
}
