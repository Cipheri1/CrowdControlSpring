package br.com.crowdcontrol.repository.jdbc;

import br.com.crowdcontrol.config.ConnectionFactory;
import br.com.crowdcontrol.model.Chamado;
import br.com.crowdcontrol.model.StatusChamado;
import br.com.crowdcontrol.repository.ChamadoRepository;
import java.util.List;
import java.util.Optional;

public class JdbcChamadoRepository implements ChamadoRepository {

    private static final String COLUNAS =
        "id, titulo, descricao, solicitante, categoria, atribuida_para, prioridade, status, data_abertura";

    private final JdbcHelper jdbc;
    private final ChamadoRowMapper mapper = new ChamadoRowMapper();

    public JdbcChamadoRepository(ConnectionFactory connectionFactory) {
        this.jdbc = new JdbcHelper(connectionFactory);
    }

    @Override
    public void salvar(Chamado c) {
        String sql = "INSERT INTO chamado (titulo, descricao, solicitante, categoria, atribuida_para, prioridade, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int id = jdbc.inserir(sql, stmt -> {
            stmt.setString(1, c.getTitulo());
            stmt.setString(2, c.getDescricao());
            stmt.setString(3, c.getSolicitante());
            stmt.setString(4, c.getCategoria());
            stmt.setString(5, c.getAtribuidaPara());
            stmt.setString(6, c.getPrioridade().getValorBanco());
            stmt.setString(7, c.getStatus().name());
        });
        c.setId(id);
    }

    @Override
    public List<Chamado> listar() {
        return jdbc.consultarLista("SELECT " + COLUNAS + " FROM chamado", mapper);
    }

    @Override
    public List<Chamado> listarPorStatus(StatusChamado status) {
        return jdbc.consultarLista(
            "SELECT " + COLUNAS + " FROM chamado WHERE status = ?",
            stmt -> stmt.setString(1, status.name()),
            mapper);
    }

    @Override
    public Optional<Chamado> buscarPorId(int id) {
        return jdbc.consultarUm(
            "SELECT " + COLUNAS + " FROM chamado WHERE id = ?",
            stmt -> stmt.setInt(1, id),
            mapper);
    }

    @Override
    public void atualizar(Chamado c) {
        String sql = "UPDATE chamado SET titulo=?, descricao=?, solicitante=?, categoria=?, atribuida_para=?, "
                   + "prioridade=?, status=? WHERE id=?";

        jdbc.executar(sql, stmt -> {
            stmt.setString(1, c.getTitulo());
            stmt.setString(2, c.getDescricao());
            stmt.setString(3, c.getSolicitante());
            stmt.setString(4, c.getCategoria());
            stmt.setString(5, c.getAtribuidaPara());
            stmt.setString(6, c.getPrioridade().getValorBanco());
            stmt.setString(7, c.getStatus().name());
            stmt.setInt(8, c.getId());
        });
    }

    @Override
    public void removerPorId(int id) {
        jdbc.executar("DELETE FROM chamado WHERE id = ?", stmt -> stmt.setInt(1, id));
    }
}
