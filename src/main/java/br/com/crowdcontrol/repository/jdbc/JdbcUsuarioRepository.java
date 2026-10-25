package br.com.crowdcontrol.repository.jdbc;

import br.com.crowdcontrol.config.ConnectionFactory;
import br.com.crowdcontrol.model.Usuario;
import br.com.crowdcontrol.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;

public class JdbcUsuarioRepository implements UsuarioRepository {

    // Colunas listadas de forma explícita (antes era SELECT *).
    private static final String COLUNAS =
        "id, nome, email, senha, cargo, departamento, status, telefone, tipo";

    private final JdbcHelper jdbc;
    private final UsuarioRowMapper mapper = new UsuarioRowMapper();

    public JdbcUsuarioRepository(ConnectionFactory connectionFactory) {
        this.jdbc = new JdbcHelper(connectionFactory);
    }

    @Override
    public void salvar(Usuario u) {
        String sql = "INSERT INTO usuario (nome, email, senha, cargo, departamento, status, telefone, tipo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int id = jdbc.inserir(sql, stmt -> {
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getSenha());
            stmt.setString(4, u.getCargo());
            stmt.setString(5, u.getDepartamento());
            stmt.setString(6, u.getStatus().getDescricao());
            stmt.setString(7, u.getTelefone());
            stmt.setString(8, u.getTipo().name());
        });
        u.setId(id);
    }

    @Override
    public List<Usuario> listar() {
        return jdbc.consultarLista("SELECT " + COLUNAS + " FROM usuario", mapper);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jdbc.consultarUm(
            "SELECT " + COLUNAS + " FROM usuario WHERE email = ?",
            stmt -> stmt.setString(1, email),
            mapper);
    }

    @Override
    public void atualizar(Usuario u) {
        String sql = "UPDATE usuario SET nome=?, senha=?, cargo=?, departamento=?, status=?, telefone=?, tipo=? "
                   + "WHERE email=?";

        jdbc.executar(sql, stmt -> {
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getSenha());
            stmt.setString(3, u.getCargo());
            stmt.setString(4, u.getDepartamento());
            stmt.setString(5, u.getStatus().getDescricao());
            stmt.setString(6, u.getTelefone());
            stmt.setString(7, u.getTipo().name());
            stmt.setString(8, u.getEmail());
        });
    }

    @Override
    public void removerPorEmail(String email) {
        jdbc.executar("DELETE FROM usuario WHERE email = ?", stmt -> stmt.setString(1, email));
    }
}
