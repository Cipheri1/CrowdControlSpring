package br.com.crowdcontrol.util;

import br.com.crowdcontrol.model.OrdemServico;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Regras de apresentação que estavam dentro de TelaPrincipal.carregarCards()
 * (cortar a descrição, formatar prazo, montar o cabeçalho do card). Fora da
 * tela, podem ser reaproveitadas pela página web e testadas sem Swing.
 */
public final class FormatadorOrdem {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private FormatadorOrdem() { }

    /** Corta a descrição no limite e acrescenta "..."; null vira texto vazio. */
    public static String resumirDescricao(String descricao, int limite) {
        if (descricao == null) {
            return "";
        }
        return descricao.length() > limite ? descricao.substring(0, limite) + "..." : descricao;
    }

    public static String formatarPrazo(LocalDate prazo) {
        return prazo != null ? prazo.format(DATA) : "Sem prazo";
    }

    public static String valorOuNaoInformado(String valor) {
        return valor != null ? valor : "N/A";
    }

    /** Exemplo: "OS-3 | EM ANDAMENTO | ALTA". */
    public static String formatarCabecalho(OrdemServico os) {
        return "OS-" + os.getId()
             + " | " + os.getStatus().name().replace("_", " ")
             + " | " + os.getPrioridade().name();
    }
}
