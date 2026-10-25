package br.com.crowdcontrol.repository.memoria;

import br.com.crowdcontrol.model.OrdemServico;
import br.com.crowdcontrol.model.StatusOs;
import br.com.crowdcontrol.repository.OrdemServicoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MemoriaOrdemServicoRepository implements OrdemServicoRepository {

    private final List<OrdemServico> dados = new ArrayList<>();
    private int proximoId = 1;

    @Override
    public void salvar(OrdemServico ordem) {
        ordem.setId(proximoId++);
        dados.add(ordem);
    }

    @Override
    public List<OrdemServico> listar() {
        return new ArrayList<>(dados);
    }

    @Override
    public List<OrdemServico> listarPorStatus(StatusOs status) {
        return dados.stream().filter(o -> o.getStatus() == status).collect(Collectors.toList());
    }

    @Override
    public Optional<OrdemServico> buscarPorId(int id) {
        return dados.stream().filter(o -> o.getId() == id).findFirst();
    }

    @Override
    public void atualizar(OrdemServico ordem) {
        for (int i = 0; i < dados.size(); i++) {
            if (dados.get(i).getId() == ordem.getId()) {
                dados.set(i, ordem);
                return;
            }
        }
    }

    @Override
    public void removerPorId(int id) {
        dados.removeIf(o -> o.getId() == id);
    }
}
