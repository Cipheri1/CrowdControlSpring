package br.com.crowdcontrol.web.dto;

import br.com.crowdcontrol.model.Usuario;
import com.fasterxml.jackson.annotation.JsonInclude;

/** Formato do usuário na API. A senha só entra na requisição; na resposta ela é omitida (null). */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UsuarioDto(Integer id, String nome, String email, String senha, String cargo, String status) {

    public static UsuarioDto de(Usuario u) {
        return new UsuarioDto(u.getId(), u.getNome(), u.getEmail(), null, u.getCargo(),
                Rotulos.statusUsuario(u.getStatus()));
    }
}
