package br.com.crowdcontrol.repository;

import br.com.crowdcontrol.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    /** Grava o usuário e preenche o id gerado no próprio objeto. */
    void salvar(Usuario usuario);

    List<Usuario> listar();

    Optional<Usuario> buscarPorEmail(String email);

    void atualizar(Usuario usuario);

    void removerPorEmail(String email);
}
