package br.com.crowdcontrol.service;

import br.com.crowdcontrol.exception.CredenciaisInvalidasException;
import br.com.crowdcontrol.model.Usuario;
import br.com.crowdcontrol.repository.UsuarioRepository;
import br.com.crowdcontrol.util.Texto;

/**
 * Antes, a conferência de senha ficava dentro de TelaLogin.btnEntrarActionPerformed.
 * Agora a regra de login vive aqui e a tela só chama autenticar().
 */
public class AutenticacaoService {

    private final UsuarioRepository repository;

    public AutenticacaoService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario autenticar(String email, String senha) {
        if (Texto.estaVazio(email) || senha == null) {
            throw new CredenciaisInvalidasException();
        }

        Usuario usuario = repository.buscarPorEmail(email)
                .orElseThrow(CredenciaisInvalidasException::new);

        // Mantém a regra original (comparação direta). Na etapa 9, trocar por hash (BCrypt).
        if (!usuario.getSenha().equals(senha)) {
            throw new CredenciaisInvalidasException();
        }
        return usuario;
    }
}
