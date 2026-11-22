package tn.iit.stages.controller.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tn.iit.stages.exception.BusinessException;
import tn.iit.stages.model.EtapeProgression;
import tn.iit.stages.model.Livrable;
import tn.iit.stages.model.Observation;
import tn.iit.stages.model.Reunion;
import tn.iit.stages.model.StatutStage;
import tn.iit.stages.model.Stage;
import tn.iit.stages.model.TypeObservation;
import tn.iit.stages.service.EnseignantService;
import tn.iit.stages.service.EntrepriseService;
import tn.iit.stages.service.EtapeProgressionService;
import tn.iit.stages.service.EtudiantService;
import tn.iit.stages.service.LivrableService;
import tn.iit.stages.service.ObservationService;
import tn.iit.stages.service.ReunionService;
import tn.iit.stages.service.StageService;

/**
 * Interface SSR (Thymeleaf) de gestion des stages et de leur suivi :
 * reunions, observations, etapes de progression et livrables.
 */
@Controller
@RequestMapping("/stages")
public class StageMvcController {

    private final StageService stageService;
    private final EntrepriseService entrepriseService;
    private final EtudiantService etudiantService;
    private final EnseignantService enseignantService;
    private final ReunionService reunionService;
    private final ObservationService observationService;
    private final EtapeProgressionService etapeService;
    private final LivrableService livrableService;

    public StageMvcController(StageService stageService,
                              EntrepriseService entrepriseService,
                              EtudiantService etudiantService,
                              EnseignantService enseignantService,
                              ReunionService reunionService,
                              ObservationService observationService,
                              EtapeProgressionService etapeService,
                              LivrableService livrableService) {
        this.stageService = stageService;
        this.entrepriseService = entrepriseService;
        this.etudiantService = etudiantService;
        this.enseignantService = enseignantService;
        this.reunionService = reunionService;
        this.observationService = observationService;
        this.etapeService = etapeService;
        this.livrableService = livrableService;
    }

    /**
     * Empeche la liaison automatique du parametre d'URL {id} (identifiant du stage)
     * sur l'attribut "id" des entites enfants (Reunion, Observation, ...).
     */
    @InitBinder
    public void interdireLiaisonId(WebDataBinder binder) {
        binder.setDisallowedFields("id");
    }

    /* ----------------------- CRUD stage ----------------------- */

    @GetMapping
    public String lister(@RequestParam(required = false) StatutStage statut,
                         @RequestParam(required = false) String sujet,
                         Model model) {
        model.addAttribute("stages", sujet != null && !sujet.isBlank()
                ? stageService.rechercher(sujet)
                : stageService.findByStatut(statut));
        model.addAttribute("statuts", StatutStage.values());
        model.addAttribute("statutSelectionne", statut);
        model.addAttribute("sujet", sujet);
        return "stages/list";
    }

    @GetMapping("/new")
    public String formulaireCreation(Model model) {
        model.addAttribute("stage", new Stage());
        preparerFormulaire(model);
        return "stages/form";
    }

    @PostMapping
    public String creer(@Valid @ModelAttribute("stage") Stage stage,
                        BindingResult result,
                        @RequestParam Long entrepriseId,
                        @RequestParam(required = false) Long etudiantId,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            preparerFormulaire(model);
            return "stages/form";
        }
        try {
            Stage cree = stageService.create(stage, entrepriseId, etudiantId);
            redirectAttributes.addFlashAttribute("message", "Stage cree avec succes.");
            return "redirect:/stages/" + cree.getId();
        } catch (BusinessException ex) {
            result.reject("business", ex.getMessage());
            preparerFormulaire(model);
            return "stages/form";
        }
    }

    @GetMapping("/{id}")
    public String consulter(@PathVariable Long id, Model model) {
        model.addAttribute("stage", stageService.getById(id));
        model.addAttribute("reunion", new Reunion());
        model.addAttribute("observation", new Observation());
        model.addAttribute("etape", new EtapeProgression());
        model.addAttribute("livrable", new Livrable());
        model.addAttribute("enseignants", enseignantService.findAll());
        model.addAttribute("typesObservation", TypeObservation.values());
        return "stages/details";
    }

    @GetMapping("/{id}/edit")
    public String formulaireEdition(@PathVariable Long id, Model model) {
        model.addAttribute("stage", stageService.getById(id));
        preparerFormulaire(model);
        return "stages/form";
    }

    @PostMapping("/{id}")
    public String modifier(@PathVariable Long id,
                           @Valid @ModelAttribute("stage") Stage stage,
                           BindingResult result,
                           @RequestParam Long entrepriseId,
                           @RequestParam(required = false) Long etudiantId,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            preparerFormulaire(model);
            return "stages/form";
        }
        try {
            stageService.update(id, stage, entrepriseId, etudiantId);
            redirectAttributes.addFlashAttribute("message", "Stage mis a jour.");
            return "redirect:/stages/" + id;
        } catch (BusinessException ex) {
            result.reject("business", ex.getMessage());
            preparerFormulaire(model);
            return "stages/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            stageService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Stage supprime.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/stages";
    }

    /* ------------------ Suivi : sous-ressources ------------------ */

    @PostMapping("/{id}/reunions")
    public String ajouterReunion(@PathVariable Long id,
                                 @Valid @ModelAttribute("reunion") Reunion reunion,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", premiereErreur(result));
        } else {
            reunionService.add(id, reunion);
            redirectAttributes.addFlashAttribute("message", "Reunion enregistree.");
        }
        return "redirect:/stages/" + id;
    }

    @PostMapping("/{id}/reunions/{reunionId}/delete")
    public String supprimerReunion(@PathVariable Long id,
                                   @PathVariable Long reunionId,
                                   RedirectAttributes redirectAttributes) {
        reunionService.delete(reunionId);
        redirectAttributes.addFlashAttribute("message", "Reunion supprimee.");
        return "redirect:/stages/" + id;
    }

    @PostMapping("/{id}/observations")
    public String ajouterObservation(@PathVariable Long id,
                                     @Valid @ModelAttribute("observation") Observation observation,
                                     BindingResult result,
                                     @RequestParam(required = false) Long auteurId,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", premiereErreur(result));
        } else {
            observationService.add(id, auteurId, observation);
            redirectAttributes.addFlashAttribute("message", "Observation enregistree.");
        }
        return "redirect:/stages/" + id;
    }

    @PostMapping("/{id}/etapes")
    public String ajouterEtape(@PathVariable Long id,
                               @Valid @ModelAttribute("etape") EtapeProgression etape,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", premiereErreur(result));
        } else {
            etapeService.add(id, etape);
            redirectAttributes.addFlashAttribute("message", "Etape ajoutee.");
        }
        return "redirect:/stages/" + id;
    }

    @PostMapping("/{id}/etapes/{etapeId}/valider")
    public String validerEtape(@PathVariable Long id,
                               @PathVariable Long etapeId,
                               @RequestParam Long enseignantId,
                               RedirectAttributes redirectAttributes) {
        try {
            etapeService.valider(etapeId, enseignantId);
            redirectAttributes.addFlashAttribute("message", "Etape validee.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/stages/" + id;
    }

    @PostMapping("/{id}/livrables")
    public String ajouterLivrable(@PathVariable Long id,
                                  @Valid @ModelAttribute("livrable") Livrable livrable,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", premiereErreur(result));
        } else {
            livrableService.add(id, livrable);
            redirectAttributes.addFlashAttribute("message", "Livrable depose.");
        }
        return "redirect:/stages/" + id;
    }

    /* -------------------------- helpers ------------------------- */

    private void preparerFormulaire(Model model) {
        model.addAttribute("entreprises", entrepriseService.findAll());
        model.addAttribute("etudiants", etudiantService.findAll());
        model.addAttribute("statuts", StatutStage.values());
    }

    private String premiereErreur(BindingResult result) {
        return result.getAllErrors().isEmpty()
                ? "Donnees invalides."
                : result.getAllErrors().get(0).getDefaultMessage();
    }
}
