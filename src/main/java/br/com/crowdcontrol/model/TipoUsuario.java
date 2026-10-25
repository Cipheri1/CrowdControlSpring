package br.com.crowdcontrol.model;

public enum TipoUsuario {

    KEY_USER("Key_user"),
    ADMINISTRADOR("Administrador"),
    FUNCIONARIO("Funcionario");

    private final String descricao;

    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
