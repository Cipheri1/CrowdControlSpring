package br.com.crowdcontrol.service;

/** Números exibidos no dashboard, já calculados (nada de Swing aqui). */
public record ResumoDashboard(int totalUsuarios,
                              int usuariosAtivos,
                              int totalOrdens,
                              int ordensPendentes,
                              int ordensEmAndamento,
                              int ordensConcluidas) {

    public boolean temPendencias() {
        return ordensPendentes > 0;
    }

    /** Texto do alerta principal (antes montado dentro da tela). */
    public String mensagemPendencias() {
        return temPendencias()
            ? "Você tem " + ordensPendentes + " ordem(ns) de serviço pendente(s)"
            : "Nenhuma ordem pendente";
    }

    public String orientacaoPendencias() {
        return temPendencias()
            ? "Atribua essas ordens aos funcionários para iniciar o trabalho"
            : "Sistema operando normalmente, ótimo trabalho!";
    }
}
