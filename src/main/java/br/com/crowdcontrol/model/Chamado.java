package br.com.crowdcontrol.model;

import java.time.LocalDateTime;

public class Chamado {

    private int id;
    private String titulo;
    private String descricao;
    private String solicitante;
    private String categoria;
    private String atribuidaPara;
    private Prioridade prioridade;
    private StatusChamado status;
    private LocalDateTime dataAbertura;

    // Um único construtor (antes havia dois, um sem descrição).
    public Chamado(String titulo, String descricao, String solicitante,
                   String categoria, Prioridade prioridade, StatusChamado status) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.solicitante = solicitante;
        this.categoria = categoria;
        this.prioridade = prioridade;
        this.status = status;
        this.dataAbertura = LocalDateTime.now();
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getSolicitante() { return solicitante; }
    public String getCategoria() { return categoria; }
    public String getAtribuidaPara() { return atribuidaPara; }
    public Prioridade getPrioridade() { return prioridade; }
    public StatusChamado getStatus() { return status; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }

    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setAtribuidaPara(String atribuidaPara) { this.atribuidaPara = atribuidaPara; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }
    public void setStatus(StatusChamado status) { this.status = status; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }

    @Override
    public String toString() {
        return titulo + " - " + status.name().replace("_", " ");
    }
}
