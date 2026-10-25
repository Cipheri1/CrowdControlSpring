package br.com.crowdcontrol.model;

public enum StatusOs {

    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDA("Concluída"),
    FINALIZADA("Finalizada");

    private final String descricao;

    StatusOs(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
