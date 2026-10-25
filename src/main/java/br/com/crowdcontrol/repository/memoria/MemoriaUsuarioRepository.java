package br.com.crowdcontrol.repository.memoria;

import br.com.crowdcontrol.model.Usuario;
import br.com.crowdcontrol.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação em memória da mesma interface do JDBC. Serve para testar os
 * services sem precisar do MySQL (graças à inversão de dependência).
 */
public class MemoriaUsuarioRepository implements UsuarioRepository {

    private final List<Usuario> dados = new ArrayList<>();
    private int proximoId = 1;

    @Override
    public void salvar(Usuario usuario) {
        usuario.setId(proximoId++);
        dados.add(usuario);
    }

    @Override
    public List<Usuario> listar() {
        return new ArrayList<>(dados);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return dados.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public void atualizar(Usuario usuario) {
        for (int i = 0; i < dados.size(); i++) {
            if (dados.get(i).getEmail().equalsIgnoreCase(usuario.getEmail())) {
                usuario.setId(dados.get(i).getId());
                dados.set(i, usuario);
                return;
            }
        }
    }

    @Override
    public void removerPorEmail(String email) {
        dados.removeIf(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
