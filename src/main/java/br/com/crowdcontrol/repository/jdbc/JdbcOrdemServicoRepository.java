package br.com.crowdcontrol.repository.jdbc;

import br.com.crowdcontrol.config.ConnectionFactory;
import br.com.crowdcontrol.model.OrdemServico;
import br.com.crowdcontrol.model.StatusOs;
import br.com.crowdcontrol.repository.OrdemServicoRepository;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcOrdemServicoRepository implements OrdemServicoRepository {

    private static final String COLUNAS =
        "id, titulo, descricao, status, prioridade, solicitante, categoria, atribuida_para, local, prazo";

    private final JdbcHelper jdbc;
    private final OrdemServicoRowMapper mapper = new OrdemServicoRowMapper();

    public JdbcOrdemServicoRepository(ConnectionFactory connectionFactory) {
        this.jdbc = new JdbcHelper(connectionFactory);
    }

    @Override
    public void salvar(OrdemServico os) {
        String sql = "INSERT INTO ordem_servico "
                   + "(titulo, descricao, status, prioridade, solicitante, categoria, atribuida_para, local, prazo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int id = jdbc.inserir(sql, stmt -> {
            stmt.setString(1, os.getTitulo());
            stmt.setString(2, os.getDescricao());
            stmt.setString(3, os.getStatus().name());
            stmt.setString(4, os.getPrioridade().getValorBanco());
            stmt.setString(5, os.getSolicitante());
            stmt.setString(6, os.getCategoria());
            stmt.setString(7, os.getAtribuidaPara());
            stmt.setString(8, os.getLocal());
            definirPrazo(stmt, 9, os);
        });
        os.setId(id);
    }

    @Override
    public List<OrdemServico> listar() {
        return jdbc.consultarLista("SELECT " + COLUNAS + " FROM ordem_servico", mapper);
    }

    @Override
    public List<OrdemServico> listarPorStatus(StatusOs status) {
        return jdbc.consultarLista(
            "SELECT " + COLUNAS + " FROM ordem_servico WHERE status = ?",
            stmt -> stmt.setString(1, status.name()),
            mapper);
    }

    @Override
    public Optional<OrdemServico> buscarPorId(int id) {
        return jdbc.consultarUm(
            "SELECT " + COLUNAS + " FROM ordem_servico WHERE id = ?",
            stmt -> stmt.setInt(1, id),
            mapper);
    }

    // Antes a atualização e a exclusão usavam o TÍTULO (que não é único no banco).
    // Agora usam o id, que identifica exatamente uma ordem.
    @Override
    public void atualizar(OrdemServico os) {
        String sql = "UPDATE ordem_servico SET titulo=?, descricao=?, status=?, prioridade=?, solicitante=?, "
                   + "categoria=?, atribuida_para=?, local=?, prazo=? WHERE id=?";

        jdbc.executar(sql, stmt -> {
            stmt.setString(1, os.getTitulo());
            stmt.setString(2, os.getDescricao());
            stmt.setString(3, os.getStatus().name());
            stmt.setString(4, os.getPrioridade().getValorBanco());
            stmt.setString(5, os.getSolicitante());
            stmt.setString(6, os.getCategoria());
            stmt.setString(7, os.getAtribuidaPara());
            stmt.setString(8, os.getLocal());
            definirPrazo(stmt, 9, os);
            stmt.setInt(10, os.getId());
        });
    }

    @Override
    public void removerPorId(int id) {
        jdbc.executar("DELETE FROM ordem_servico WHERE id = ?", stmt -> stmt.setInt(1, id));
    }

    private void definirPrazo(PreparedStatement stmt, int indice, OrdemServico os) throws SQLException {
        if (os.getPrazo() != null) {
            stmt.setDate(indice, Date.valueOf(os.getPrazo()));
        } else {
            stmt.setNull(indice, java.sql.Types.DATE);
        }
    }
}
