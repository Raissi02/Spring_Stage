package tn.iit.stages.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tn.iit.stages.service.StageService;

@Controller
public class HomeController {

    private final StageService stageService;

    public HomeController(StageService stageService) {
        this.stageService = stageService;
    }

    /** Page d'accueil : choix de l'espace (administrateur, enseignant, etudiant). */
    @GetMapping("/")
    public String accueil(Model model) {
        model.addAttribute("stats", stageService.statistiques());
        return "accueil";
    }

    /** Espace administrateur : tableau de bord et statistiques. */
    @GetMapping("/admin")
    public String tableauDeBord(Model model) {
        model.addAttribute("stats", stageService.statistiques());
        model.addAttribute("stages", stageService.findAll());
        return "index";
    }
}
