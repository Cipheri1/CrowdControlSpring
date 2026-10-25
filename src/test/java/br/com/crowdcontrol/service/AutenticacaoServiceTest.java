package br.com.crowdcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.crowdcontrol.exception.CredenciaisInvalidasException;
import br.com.crowdcontrol.model.StatusUsuario;
import br.com.crowdcontrol.model.TipoUsuario;
import br.com.crowdcontrol.model.Usuario;
import br.com.crowdcontrol.repository.memoria.MemoriaUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AutenticacaoServiceTest {

    private AutenticacaoService service;

    // Roda antes de cada teste: repositório novo, com um usuário cadastrado.
    @BeforeEach
    void preparar() {
        MemoriaUsuarioRepository repositorio = new MemoriaUsuarioRepository();
        repositorio.salvar(new Usuario("Matheus", "matheus@gmail.com", "123", "Dev", "TI",
                StatusUsuario.ATIVO, "0000", TipoUsuario.KEY_USER));
        service = new AutenticacaoService(repositorio);
    }

    @Test
    @DisplayName("Login com e-mail e senha corretos devolve o usuário")
    void loginCorreto() {
        Usuario logado = service.autenticar("matheus@gmail.com", "123");

        assertEquals("Matheus", logado.getNome());
    }

    @Test
    @DisplayName("Senha errada é rejeitada")
    void senhaErrada() {
        assertThrows(CredenciaisInvalidasException.class,
            () -> service.autenticar("matheus@gmail.com", "errada"));
    }

    @Test
    @DisplayName("E-mail não cadastrado é rejeitado")
    void emailInexistente() {
        assertThrows(CredenciaisInvalidasException.class,
            () -> service.autenticar("ninguem@gmail.com", "123"));
    }

    @Test
    @DisplayName("E-mail vazio ou senha nula são rejeitados")
    void camposVazios() {
        assertThrows(CredenciaisInvalidasException.class, () -> service.autenticar("", "123"));
        assertThrows(CredenciaisInvalidasException.class, () -> service.autenticar(null, "123"));
        assertThrows(CredenciaisInvalidasException.class,
            () -> service.autenticar("matheus@gmail.com", null));
    }

    @Test
    @DisplayName("A mensagem de erro é a mesma para e-mail inexistente e senha errada")
    void mensagemIgualParaOsDoisErros() {
        CredenciaisInvalidasException semUsuario = assertThrows(CredenciaisInvalidasException.class,
            () -> service.autenticar("ninguem@gmail.com", "123"));
        CredenciaisInvalidasException senhaErrada = assertThrows(CredenciaisInvalidasException.class,
            () -> service.autenticar("matheus@gmail.com", "errada"));

        assertEquals(semUsuario.getMessage(), senhaErrada.getMessage());
    }
}
