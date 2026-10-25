package br.com.crowdcontrol.repository.memoria;

import br.com.crowdcontrol.model.Chamado;
import br.com.crowdcontrol.model.StatusChamado;
import br.com.crowdcontrol.repository.ChamadoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MemoriaChamadoRepository implements ChamadoRepository {

    private final List<Chamado> dados = new ArrayList<>();
    private int proximoId = 1;

    @Override
    public void salvar(Chamado chamado) {
        chamado.setId(proximoId++);
        dados.add(chamado);
    }

    @Override
    public List<Chamado> listar() {
        return new ArrayList<>(dados);
    }

    @Override
    public List<Chamado> listarPorStatus(StatusChamado status) {
        return dados.stream().filter(c -> c.getStatus() == status).collect(Collectors.toList());
    }

    @Override
    public Optional<Chamado> buscarPorId(int id) {
        return dados.stream().filter(c -> c.getId() == id).findFirst();
    }

    @Override
    public void atualizar(Chamado chamado) {
        for (int i = 0; i < dados.size(); i++) {
            if (dados.get(i).getId() == chamado.getId()) {
                dados.set(i, chamado);
                return;
            }
        }
    }

    @Override
    public void removerPorId(int id) {
        dados.removeIf(c -> c.getId() == id);
    }
}
