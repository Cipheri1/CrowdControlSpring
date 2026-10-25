package br.com.crowdcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.crowdcontrol.exception.RegraNegocioException;
import br.com.crowdcontrol.model.StatusUsuario;
import br.com.crowdcontrol.model.TipoUsuario;
import br.com.crowdcontrol.model.Usuario;
import br.com.crowdcontrol.repository.memoria.MemoriaUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsuarioServiceTest {

    private UsuarioService service;

    @BeforeEach
    void preparar() {
        service = new UsuarioService(new MemoriaUsuarioRepository());
    }

    private static Usuario usuario(String nome, String email, String senha) {
        return new Usuario(nome, email, senha, "Cargo", "Depto",
                StatusUsuario.ATIVO, "0000", TipoUsuario.FUNCIONARIO);
    }

    @Test
    @DisplayName("Cria usuário válido e gera um id")
    void criaUsuarioValido() {
        Usuario ana = usuario("Ana", "ana@gmail.com", "123");

        service.criarUsuario(ana);

        assertTrue(ana.getId() > 0);
        assertEquals(1, service.listarUsuarios().size());
    }

    @Test
    @DisplayName("Rejeita e-mail já cadastrado")
    void rejeitaEmailDuplicado() {
        service.criarUsuario(usuario("Ana", "ana@gmail.com", "123"));

        RegraNegocioException erro = assertThrows(RegraNegocioException.class,
            () -> service.criarUsuario(usuario("Outra", "ana@gmail.com", "456")));

        assertEquals("Email já cadastrado!", erro.getMessage());
    }

    @Test
    @DisplayName("Rejeita nome vazio")
    void rejeitaNomeVazio() {
        assertThrows(RegraNegocioException.class,
            () -> service.criarUsuario(usuario("  ", "a@x.com", "123")));
    }

    @Test
    @DisplayName("Rejeita e-mail sem @")
    void rejeitaEmailInvalido() {
        assertThrows(RegraNegocioException.class,
            () -> service.criarUsuario(usuario("Ana", "semarroba", "123")));
    }

    @Test
    @DisplayName("Rejeita senha vazia")
    void rejeitaSenhaVazia() {
        assertThrows(RegraNegocioException.class,
            () -> service.criarUsuario(usuario("Ana", "ana@gmail.com", "")));
    }

    @Test
    @DisplayName("Rejeita usuário nulo")
    void rejeitaUsuarioNulo() {
        assertThrows(RegraNegocioException.class, () -> service.criarUsuario(null));
    }

    @Test
    @DisplayName("Atualiza os dados de um usuário existente")
    void atualizaUsuario() {
        Usuario ana = usuario("Ana", "ana@gmail.com", "123");
        service.criarUsuario(ana);

        ana.setCargo("Diretora");
        service.atualizarUsuario(ana);

        assertEquals("Diretora", service.buscarPorEmail("ana@gmail.com").get().getCargo());
    }

    @Test
    @DisplayName("Rejeita atualizar usuário que não existe")
    void rejeitaAtualizarInexistente() {
        assertThrows(RegraNegocioException.class,
            () -> service.atualizarUsuario(usuario("Ana", "ana@gmail.com", "123")));
    }

    @Test
    @DisplayName("Exclui usuário existente e rejeita excluir de novo")
    void excluiUsuario() {
        service.criarUsuario(usuario("Ana", "ana@gmail.com", "123"));

        service.excluirUsuario("ana@gmail.com");

        assertTrue(service.buscarPorEmail("ana@gmail.com").isEmpty());
        assertThrows(RegraNegocioException.class, () -> service.excluirUsuario("ana@gmail.com"));
    }
}
