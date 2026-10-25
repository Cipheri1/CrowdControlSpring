package br.com.crowdcontrol.service;

import br.com.crowdcontrol.exception.RegraNegocioException;
import br.com.crowdcontrol.model.OrdemServico;
import br.com.crowdcontrol.model.StatusOs;
import br.com.crowdcontrol.repository.OrdemServicoRepository;
import br.com.crowdcontrol.util.Texto;
import java.util.List;
import java.util.Optional;

public class OrdemServicoService {

    private final OrdemServicoRepository repository;

    public OrdemServicoService(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    public void criarOrdem(OrdemServico ordem) {
        validar(ordem);
        repository.salvar(ordem);
    }

    public List<OrdemServico> listarOrdens() {
        return repository.listar();
    }

    public Optional<OrdemServico> buscarPorId(int id) {
        validarId(id);
        return repository.buscarPorId(id);
    }

    public void atualizarOrdem(OrdemServico ordem) {
        validar(ordem);
        validarId(ordem.getId());

        if (repository.buscarPorId(ordem.getId()).isEmpty()) {
            throw new RegraNegocioException("Ordem não encontrada!");
        }
        repository.atualizar(ordem);
    }

    public void excluirOrdem(int id) {
        validarId(id);
        repository.removerPorId(id);
    }

    public List<OrdemServico> listarPorStatus(StatusOs status) {
        if (status == null) {
            throw new RegraNegocioException("Status não pode ser nulo");
        }
        return repository.listarPorStatus(status);
    }

    private void validar(OrdemServico ordem) {
        if (ordem == null) {
            throw new RegraNegocioException("Ordem não pode ser nula");
        }
        if (Texto.estaVazio(ordem.getTitulo())) {
            throw new RegraNegocioException("Título é obrigatório");
        }
        if (ordem.getPrioridade() == null || ordem.getStatus() == null) {
            throw new RegraNegocioException("Prioridade e status são obrigatórios");
        }
    }

    private void validarId(int id) {
        if (id <= 0) {
            throw new RegraNegocioException("ID inválido");
        }
    }
}
