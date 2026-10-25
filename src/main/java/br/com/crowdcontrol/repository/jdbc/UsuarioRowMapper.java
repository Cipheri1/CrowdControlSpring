package br.com.crowdcontrol.repository.jdbc;

import br.com.crowdcontrol.model.StatusUsuario;
import br.com.crowdcontrol.model.TipoUsuario;
import br.com.crowdcontrol.model.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRowMapper implements RowMapper<Usuario> {

    @Override
    public Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario(
            rs.getString("nome"),
            rs.getString("email"),
            rs.getString("senha"),
            rs.getString("cargo"),
            rs.getString("departamento"),
            StatusUsuario.deBanco(rs.getString("status")),
            rs.getString("telefone"),
            TipoUsuario.valueOf(rs.getString("tipo"))
        );
        u.setId(rs.getInt("id"));
        return u;
    }
}
