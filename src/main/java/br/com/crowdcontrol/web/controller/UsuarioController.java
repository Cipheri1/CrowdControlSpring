package br.com.crowdcontrol.web.controller;

import br.com.crowdcontrol.exception.RegraNegocioException;
import br.com.crowdcontrol.model.StatusUsuario;
import br.com.crowdcontrol.model.TipoUsuario;
import br.com.crowdcontrol.model.Usuario;
import br.com.crowdcontrol.service.UsuarioService;
import br.com.crowdcontrol.web.dto.Rotulos;
import br.com.crowdcontrol.web.dto.UsuarioDto;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioDto> listar() {
        return service.listarUsuarios().stream().map(UsuarioDto::de).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDto criar(@RequestBody UsuarioDto dados) {
        // O formulário não pede tipo, departamento nem telefone: novo usuário entra como FUNCIONARIO.
        Usuario u = new Usuario(dados.nome(), dados.email(), dados.senha(), dados.cargo(), null,
                Rotulos.paraEnum(StatusUsuario.class, dados.status()), null, TipoUsuario.FUNCIONARIO);
        service.criarUsuario(u);
        return UsuarioDto.de(u);
    }

    // O usuário é identificado pelo e-mail (único no banco), como no service da etapa 6.
    @PutMapping("/{email:.+}")
    public UsuarioDto atualizar(@PathVariable String email, @RequestBody UsuarioDto dados) {
        Usuario u = service.buscarPorEmail(email)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado!"));
        u.setNome(dados.nome());
        u.setCargo(dados.cargo());
        u.setStatus(Rotulos.paraEnum(StatusUsuario.class, dados.status()));
        if (dados.senha() != null && !dados.senha().isBlank()) {
            u.setSenha(dados.senha()); // senha em branco = manter a atual
        }
        service.atualizarUsuario(u);
        return UsuarioDto.de(u);
    }

    @DeleteMapping("/{email:.+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable String email) {
        service.excluirUsuario(email);
    }
}
