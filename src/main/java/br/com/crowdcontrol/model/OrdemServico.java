package br.com.crowdcontrol.model;

import java.time.LocalDate;

/**
 * Antes (OrdensServico) havia 3 construtores longos com 6, 8 e 10 parâmetros.
 * Agora a criação é feita pelo padrão Builder:
 *
 *   OrdemServico os = new OrdemServico.Builder("Trocar lâmpada", Prioridade.BAIXA)
 *           .local("Sala 2").prazo(LocalDate.now().plusDays(3)).build();
 */
public class OrdemServico {

    private int id;
    private String titulo;
    private String descricao;
    private StatusOs status;
    private Prioridade prioridade;
    private LocalDate prazo;
    private String solicitante;
    private String categoria;
    private String atribuidaPara;
    private String local;

    private OrdemServico(Builder b) {
        this.id = b.id;
        this.titulo = b.titulo;
        this.descricao = b.descricao;
        this.status = b.status;
        this.prioridade = b.prioridade;
        this.prazo = b.prazo;
        this.solicitante = b.solicitante;
        this.categoria = b.categoria;
        this.atribuidaPara = b.atribuidaPara;
        this.local = b.local;
    }

    public static class Builder {
        // campos obrigatórios
        private final String titulo;
        private final Prioridade prioridade;
        // campos opcionais, com valor padrão
        private int id;
        private String descricao;
        private StatusOs status = StatusOs.PENDENTE;
        private LocalDate prazo;
        private String solicitante;
        private String categoria;
        private String atribuidaPara;
        private String local;

        public Builder(String titulo, Prioridade prioridade) {
            this.titulo = titulo;
            this.prioridade = prioridade;
        }

        public Builder id(int id) { this.id = id; return this; }
        public Builder descricao(String descricao) { this.descricao = descricao; return this; }
        public Builder status(StatusOs status) { this.status = status; return this; }
        public Builder prazo(LocalDate prazo) { this.prazo = prazo; return this; }
        public Builder solicitante(String solicitante) { this.solicitante = solicitante; return this; }
        public Builder categoria(String categoria) { this.categoria = categoria; return this; }
        public Builder atribuidaPara(String atribuidaPara) { this.atribuidaPara = atribuidaPara; return this; }
        public Builder local(String local) { this.local = local; return this; }

        public OrdemServico build() {
            return new OrdemServico(this);
        }
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public StatusOs getStatus() { return status; }
    public Prioridade getPrioridade() { return prioridade; }
    public LocalDate getPrazo() { return prazo; }
    public String getSolicitante() { return solicitante; }
    public String getCategoria() { return categoria; }
    public String getAtribuidaPara() { return atribuidaPara; }
    public String getLocal() { return local; }

    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setStatus(StatusOs status) { this.status = status; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setAtribuidaPara(String atribuidaPara) { this.atribuidaPara = atribuidaPara; }
    public void setLocal(String local) { this.local = local; }

    @Override
    public String toString() {
        return titulo + " - " + status.name().replace("_", " ");
    }
}
