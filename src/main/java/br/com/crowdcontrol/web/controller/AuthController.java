package br.com.crowdcontrol.web.controller;

import br.com.crowdcontrol.model.Usuario;
import br.com.crowdcontrol.service.AutenticacaoService;
import br.com.crowdcontrol.web.config.AuthInterceptor;
import br.com.crowdcontrol.web.dto.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AutenticacaoService autenticacao;

    public AuthController(AutenticacaoService autenticacao) {
        this.autenticacao = autenticacao;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDto dados, HttpServletRequest req) {
        Usuario usuario = autenticacao.autenticar(dados.email(), dados.senha());

        HttpSession antiga = req.getSession(false);
        if (antiga != null) {
            antiga.invalidate(); // nova sessão a cada login
        }
        req.getSession(true).setAttribute(AuthInterceptor.SESSAO_USUARIO, usuario.getNome());
        return Map.of("nome", usuario.getNome());
    }

    @PostMapping("/logout")
    public void logout(HttpSession sessao) {
        sessao.invalidate();
    }

    /** O front usa esta chamada para saber quem está logado (401 se ninguém). */
    @GetMapping("/sessao")
    public Map<String, String> sessao(HttpSession sessao) {
        return Map.of("nome", (String) sessao.getAttribute(AuthInterceptor.SESSAO_USUARIO));
    }
}
