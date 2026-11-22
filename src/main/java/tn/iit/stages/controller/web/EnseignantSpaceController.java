package tn.iit.stages.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import tn.iit.stages.model.Enseignant;
import tn.iit.stages.model.EtapeProgression;
import tn.iit.stages.model.Etudiant;
import tn.iit.stages.model.StatutEtape;
import tn.iit.stages.model.StatutStage;
import tn.iit.stages.model.Stage;
import tn.iit.stages.service.EnseignantService;
import tn.iit.stages.service.EtudiantService;

import java.util.ArrayList;
import java.util.List;

/**
 * Espace "enseignant encadrant" : selection du profil puis tableau de bord
 * des etudiants affectes et des actions de suivi.
 */
@Controller
@RequestMapping("/enseignant")
public class EnseignantSpaceController {

    private final EnseignantService enseignantService;
    private final EtudiantService etudiantService;

    public EnseignantSpaceController(EnseignantService enseignantService,
                                     EtudiantService etudiantService) {
        this.enseignantService = enseignantService;
        this.etudiantService = etudiantService;
    }

    @GetMapping
    public String choisirEnseignant(Model model) {
        model.addAttribute("enseignants", enseignantService.findAll());
        return "enseignants/choix";
    }

    @GetMapping("/{id}")
    public String espace(@PathVariable Long id, Model model) {
        Enseignant enseignant = enseignantService.getById(id);
        List<Etudiant> etudiants = etudiantService.findByEncadrant(id);

        int sansStage = 0;
        int stagesEnCours = 0;
        int reunions = 0;
        List<EtapeProgression> etapesAValider = new ArrayList<>();

        for (Etudiant etudiant : etudiants) {
            Stage stage = etudiant.getStage();
            if (stage == null) {
                sansStage++;
                continue;
            }
            if (stage.getStatut() == StatutStage.EN_COURS) {
                stagesEnCours++;
            }
            reunions += stage.getReunions().size();
            for (EtapeProgression etape : stage.getEtapes()) {
                if (etape.getStatut() != StatutEtape.VALIDEE) {
                    etapesAValider.add(etape);
                }
            }
        }

        model.addAttribute("enseignant", enseignant);
        model.addAttribute("etudiants", etudiants);
        model.addAttribute("etapesAValider", etapesAValider);
        model.addAttribute("nbEtudiants", etudiants.size());
        model.addAttribute("nbSansStage", sansStage);
        model.addAttribute("nbStagesEnCours", stagesEnCours);
        model.addAttribute("nbReunions", reunions);
        return "enseignants/espace";
    }
}
