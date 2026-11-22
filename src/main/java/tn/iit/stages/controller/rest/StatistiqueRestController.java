package tn.iit.stages.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.iit.stages.dto.StatistiquesDto;
import tn.iit.stages.service.StageService;

/**
 * Indicateurs agreges consommes par le tableau de bord et l'interface CSR.
 */
@RestController
@RequestMapping("/api/statistiques")
public class StatistiqueRestController {

    private final StageService stageService;

    public StatistiqueRestController(StageService stageService) {
        this.stageService = stageService;
    }

    @GetMapping
    public StatistiquesDto statistiques() {
        return stageService.statistiques();
    }
}
