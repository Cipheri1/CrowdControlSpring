package br.com.crowdcontrol.model;

/** Substitui a String "Ativo"/"Inativo" que ficava solta em Usuario. */
public enum StatusUsuario {
    ATIVO("Ativo"),
    INATIVO("Inativo");

    private final String descricao;

    StatusUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusUsuario deBanco(String valor) {
        if (valor == null) {
            return null;
        }
        for (StatusUsuario s : values()) {
            if (s.descricao.equalsIgnoreCase(valor)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Status de usuário desconhecido: " + valor);
    }
}
