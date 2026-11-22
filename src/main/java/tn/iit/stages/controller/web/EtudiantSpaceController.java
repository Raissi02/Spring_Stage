package tn.iit.stages.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tn.iit.stages.service.EtudiantService;

/**
 * Espace "etudiant stagiaire" : selection du profil puis redirection vers
 * l'espace etudiant existant (/etudiants/{id}).
 */
@Controller
@RequestMapping("/etudiant")
public class EtudiantSpaceController {

    private final EtudiantService etudiantService;

    public EtudiantSpaceController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @GetMapping
    public String choisirEtudiant(Model model) {
        model.addAttribute("etudiants", etudiantService.findAll());
        return "etudiants/choix";
    }
}
