package br.com.crowdcontrol.repository;

import br.com.crowdcontrol.model.Chamado;
import br.com.crowdcontrol.model.StatusChamado;
import java.util.List;
import java.util.Optional;

public interface ChamadoRepository {

    /** Grava o chamado e preenche o id gerado no próprio objeto. */
    void salvar(Chamado chamado);

    List<Chamado> listar();

    List<Chamado> listarPorStatus(StatusChamado status);

    Optional<Chamado> buscarPorId(int id);

    void atualizar(Chamado chamado);

    void removerPorId(int id);
}
