package br.com.crowdcontrol.web.dto;

import br.com.crowdcontrol.service.ResumoDashboard;

public record DashboardDto(int usuariosAtivos, int totalUsuarios, int totalOrdens, int ordensPendentes,
                           int ordensEmAndamento, int ordensConcluidas, String mensagem, String orientacao) {

    public static DashboardDto de(ResumoDashboard r) {
        return new DashboardDto(r.usuariosAtivos(), r.totalUsuarios(), r.totalOrdens(), r.ordensPendentes(),
                r.ordensEmAndamento(), r.ordensConcluidas(), r.mensagemPendencias(), r.orientacaoPendencias());
    }
}
