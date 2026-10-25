package br.com.crowdcontrol.repository;

import br.com.crowdcontrol.model.OrdemServico;
import br.com.crowdcontrol.model.StatusOs;
import java.util.List;
import java.util.Optional;

public interface OrdemServicoRepository {

    /** Grava a ordem e preenche o id gerado no próprio objeto. */
    void salvar(OrdemServico ordem);

    List<OrdemServico> listar();

    List<OrdemServico> listarPorStatus(StatusOs status);

    Optional<OrdemServico> buscarPorId(int id);

    void atualizar(OrdemServico ordem);

    void removerPorId(int id);
}
