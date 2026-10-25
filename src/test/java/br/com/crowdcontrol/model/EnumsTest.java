package br.com.crowdcontrol.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Testa a conversão entre o texto gravado no MySQL e os enums do sistema. */
class EnumsTest {

    @Test
    @DisplayName("Prioridade.deBanco converte o texto do banco")
    void prioridadeDeBanco() {
        assertEquals(Prioridade.CRITICA, Prioridade.deBanco("critica"));
        assertEquals(Prioridade.BAIXA, Prioridade.deBanco("baixa"));
    }

    @Test
    @DisplayName("Prioridade.deBanco ignora maiúsculas e minúsculas")
    void prioridadeIgnoraCaixa() {
        assertEquals(Prioridade.ALTA, Prioridade.deBanco("ALTA"));
        assertEquals(Prioridade.MEDIA, Prioridade.deBanco("Media"));
    }

    @Test
    @DisplayName("Prioridade volta para o texto usado no banco")
    void prioridadeValorBanco() {
        assertEquals("critica", Prioridade.CRITICA.getValorBanco());
    }

    @Test
    @DisplayName("Prioridade.deBanco aceita nulo e rejeita valor desconhecido")
    void prioridadeNuloEInvalido() {
        assertNull(Prioridade.deBanco(null));
        assertThrows(IllegalArgumentException.class, () -> Prioridade.deBanco("urgentissima"));
    }

    @Test
    @DisplayName("StatusUsuario.deBanco converte 'Ativo' e 'Inativo'")
    void statusUsuarioDeBanco() {
        assertEquals(StatusUsuario.ATIVO, StatusUsuario.deBanco("Ativo"));
        assertEquals(StatusUsuario.INATIVO, StatusUsuario.deBanco("inativo"));
    }

    @Test
    @DisplayName("StatusUsuario.deBanco rejeita valor desconhecido")
    void statusUsuarioInvalido() {
        assertThrows(IllegalArgumentException.class, () -> StatusUsuario.deBanco("Suspenso"));
    }
}
