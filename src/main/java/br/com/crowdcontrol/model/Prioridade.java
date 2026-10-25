package br.com.crowdcontrol.model;

/**
 * Antes a prioridade era uma String solta ("baixa", "media"...), o que permitia
 * valores inválidos. Com enum, só os valores abaixo existem.
 */
public enum Prioridade {
    BAIXA("baixa"),
    MEDIA("media"),
    ALTA("alta"),
    CRITICA("critica");

    private final String valorBanco;

    Prioridade(String valorBanco) {
        this.valorBanco = valorBanco;
    }

    /** Texto como está gravado na coluna "prioridade" do MySQL. */
    public String getValorBanco() {
        return valorBanco;
    }

    /** Converte o texto do banco para o enum (ignora maiúsculas/minúsculas). */
    public static Prioridade deBanco(String valor) {
        if (valor == null) {
            return null;
        }
        for (Prioridade p : values()) {
            if (p.valorBanco.equalsIgnoreCase(valor)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Prioridade desconhecida: " + valor);
    }
}
