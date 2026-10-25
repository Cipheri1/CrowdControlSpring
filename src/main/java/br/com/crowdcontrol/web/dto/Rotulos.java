package br.com.crowdcontrol.web.dto;

import br.com.crowdcontrol.exception.RegraNegocioException;
import br.com.crowdcontrol.model.Prioridade;
import br.com.crowdcontrol.model.StatusChamado;
import br.com.crowdcontrol.model.StatusOs;
import br.com.crowdcontrol.model.StatusUsuario;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Faz a ponte entre os textos que o front-end usa ("Em andamento", "Média", "Concluída")
 * e os enums do núcleo (EM_ANDAMENTO, MEDIA, CONCLUIDA).
 */
public final class Rotulos {

    private Rotulos() { }

    /** "Em andamento" -> EM_ANDAMENTO; "Média" -> MEDIA (ignora acentos e maiúsculas). */
    public static <E extends Enum<E>> E paraEnum(Class<E> tipo, String rotulo) {
        if (rotulo == null || rotulo.isBlank()) {
            throw new RegraNegocioException("Campo obrigatório: " + tipo.getSimpleName());
        }
        String chave = Normalizer.normalize(rotulo.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toUpperCase().replace(' ', '_');
        try {
            return Enum.valueOf(tipo, chave);
        } catch (IllegalArgumentException e) {
            throw new RegraNegocioException("Valor inválido: " + rotulo);
        }
    }

    public static String prioridade(Prioridade p) {
        if (p == null) return null;
        return switch (p) {
            case BAIXA -> "Baixa";
            case MEDIA -> "Média";
            case ALTA -> "Alta";
            case CRITICA -> "Crítica";
        };
    }

    public static String statusOs(StatusOs s) {
        return s == null ? null : s.getDescricao();
    }

    public static String statusUsuario(StatusUsuario s) {
        return s == null ? null : s.getDescricao();
    }

    public static String statusChamado(StatusChamado s) {
        if (s == null) return null;
        return switch (s) {
            case ABERTO -> "Aberto";
            case EM_ANDAMENTO -> "Em andamento";
            case RESOLVIDO -> "Resolvido";
        };
    }

    public static String vazioParaNulo(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto.trim();
    }

    public static LocalDate data(String texto) {
        if (texto == null || texto.isBlank()) return null;
        try {
            return LocalDate.parse(texto.trim());
        } catch (DateTimeParseException e) {
            throw new RegraNegocioException("Data inválida: " + texto);
        }
    }
}
