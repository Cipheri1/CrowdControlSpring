package br.com.crowdcontrol.service;

import br.com.crowdcontrol.exception.RegraNegocioException;
import br.com.crowdcontrol.model.Usuario;
import br.com.crowdcontrol.repository.UsuarioRepository;
import br.com.crowdcontrol.util.Texto;
import java.util.List;
import java.util.Optional;

public class UsuarioService {

    // Depende da INTERFACE (princípio D do SOLID), não da classe JDBC.
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void criarUsuario(Usuario usuario) {
        validar(usuario);

        if (repository.buscarPorEmail(usuario.getEmail()).isPresent()) {
            throw new RegraNegocioException("Email já cadastrado!");
        }
        repository.salvar(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return repository.listar();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.buscarPorEmail(email);
    }

    public void atualizarUsuario(Usuario usuario) {
        validar(usuario);

        if (repository.buscarPorEmail(usuario.getEmail()).isEmpty()) {
            throw new RegraNegocioException("Usuário não encontrado!");
        }
        repository.atualizar(usuario);
    }

    public void excluirUsuario(String email) {
        if (Texto.estaVazio(email)) {
            throw new RegraNegocioException("Email inválido!");
        }
        if (repository.buscarPorEmail(email).isEmpty()) {
            throw new RegraNegocioException("Usuário não encontrado!");
        }
        repository.removerPorEmail(email);
    }

    // Antes, esta validação não existia: o service só checava e-mail duplicado.
    private void validar(Usuario u) {
        if (u == null) {
            throw new RegraNegocioException("Usuário não pode ser nulo");
        }
        if (Texto.estaVazio(u.getNome())) {
            throw new RegraNegocioException("Nome é obrigatório");
        }
        if (Texto.estaVazio(u.getEmail()) || !u.getEmail().contains("@")) {
            throw new RegraNegocioException("Email inválido");
        }
        if (Texto.estaVazio(u.getSenha())) {
            throw new RegraNegocioException("Senha é obrigatória");
        }
        if (u.getStatus() == null || u.getTipo() == null) {
            throw new RegraNegocioException("Status e tipo do usuário são obrigatórios");
        }
    }
}
