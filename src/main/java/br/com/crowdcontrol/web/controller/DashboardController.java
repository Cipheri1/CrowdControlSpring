package br.com.crowdcontrol.web.controller;

import br.com.crowdcontrol.service.DashboardService;
import br.com.crowdcontrol.web.dto.DashboardDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping
    public DashboardDto resumo() {
        return DashboardDto.de(service.gerarResumo());
    }
}
