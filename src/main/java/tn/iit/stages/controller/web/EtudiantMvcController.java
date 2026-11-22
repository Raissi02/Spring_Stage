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
import tn.iit.stages.model.DeclarationAvancement;
import tn.iit.stages.model.Etudiant;
import tn.iit.stages.model.Livrable;
import tn.iit.stages.service.DeclarationAvancementService;
import tn.iit.stages.service.EnseignantService;
import tn.iit.stages.service.EtudiantService;
import tn.iit.stages.service.LivrableService;

@Controller
@RequestMapping("/etudiants")
public class EtudiantMvcController {

    private final EtudiantService etudiantService;
    private final EnseignantService enseignantService;
    private final DeclarationAvancementService declarationService;
    private final LivrableService livrableService;

    public EtudiantMvcController(EtudiantService etudiantService,
                                 EnseignantService enseignantService,
                                 DeclarationAvancementService declarationService,
                                 LivrableService livrableService) {
        this.etudiantService = etudiantService;
        this.enseignantService = enseignantService;
        this.declarationService = declarationService;
        this.livrableService = livrableService;
    }

    @InitBinder
    public void interdireLiaisonId(WebDataBinder binder) {
        binder.setDisallowedFields("id");
    }

    @GetMapping
    public String lister(@RequestParam(required = false) String motCle, Model model) {
        model.addAttribute("etudiants", etudiantService.rechercher(motCle));
        model.addAttribute("motCle", motCle);
        return "etudiants/list";
    }

    @GetMapping("/new")
    public String formulaireCreation(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        preparerFormulaire(model);
        return "etudiants/form";
    }

    @PostMapping
    public String creer(@Valid @ModelAttribute("etudiant") Etudiant etudiant,
                        BindingResult result,
                        @RequestParam(required = false) Long encadrantId,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            preparerFormulaire(model);
            return "etudiants/form";
        }
        try {
            etudiantService.create(etudiant, encadrantId);
            redirectAttributes.addFlashAttribute("message", "Etudiant ajoute avec succes.");
            return "redirect:/etudiants";
        } catch (BusinessException ex) {
            result.rejectValue("email", "business", ex.getMessage());
            preparerFormulaire(model);
            return "etudiants/form";
        }
    }

    /** Espace etudiant : informations du stage, avancement, livrables, observations. */
    @GetMapping("/{id}")
    public String consulter(@PathVariable Long id, Model model) {
        model.addAttribute("etudiant", etudiantService.getById(id));
        model.addAttribute("declaration", new DeclarationAvancement());
        model.addAttribute("livrable", new Livrable());
        return "etudiants/details";
    }

    @GetMapping("/{id}/edit")
    public String formulaireEdition(@PathVariable Long id, Model model) {
        model.addAttribute("etudiant", etudiantService.getById(id));
        preparerFormulaire(model);
        return "etudiants/form";
    }

    @PostMapping("/{id}")
    public String modifier(@PathVariable Long id,
                           @Valid @ModelAttribute("etudiant") Etudiant etudiant,
                           BindingResult result,
                           @RequestParam(required = false) Long encadrantId,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            preparerFormulaire(model);
            return "etudiants/form";
        }
        try {
            etudiantService.update(id, etudiant, encadrantId);
            redirectAttributes.addFlashAttribute("message", "Etudiant mis a jour.");
            return "redirect:/etudiants";
        } catch (BusinessException ex) {
            result.rejectValue("email", "business", ex.getMessage());
            preparerFormulaire(model);
            return "etudiants/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            etudiantService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Etudiant supprime.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/etudiants";
    }

    /* ------------------- Actions de l'etudiant stagiaire ------------------- */

    /** "Declarer l'avancement de ses travaux". */
    @PostMapping("/{id}/avancement")
    public String declarerAvancement(@PathVariable Long id,
                                     @Valid @ModelAttribute("declaration") DeclarationAvancement declaration,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        Etudiant etudiant = etudiantService.getById(id);
        if (etudiant.getStage() == null) {
            redirectAttributes.addFlashAttribute("error",
                    "Aucun stage n'est associe a cet etudiant.");
            return "redirect:/etudiants/" + id;
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", premiereErreur(result));
            return "redirect:/etudiants/" + id;
        }
        declarationService.add(etudiant.getStage().getId(), declaration);
        redirectAttributes.addFlashAttribute("message", "Avancement declare avec succes.");
        return "redirect:/etudiants/" + id;
    }

    /** "Deposer les informations ou liens vers ses livrables". */
    @PostMapping("/{id}/livrables")
    public String deposerLivrable(@PathVariable Long id,
                                  @Valid @ModelAttribute("livrable") Livrable livrable,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        Etudiant etudiant = etudiantService.getById(id);
        if (etudiant.getStage() == null) {
            redirectAttributes.addFlashAttribute("error",
                    "Aucun stage n'est associe a cet etudiant.");
            return "redirect:/etudiants/" + id;
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", premiereErreur(result));
            return "redirect:/etudiants/" + id;
        }
        livrableService.add(etudiant.getStage().getId(), livrable);
        redirectAttributes.addFlashAttribute("message", "Livrable depose avec succes.");
        return "redirect:/etudiants/" + id;
    }

    private void preparerFormulaire(Model model) {
        model.addAttribute("enseignants", enseignantService.findAll());
    }

    private String premiereErreur(BindingResult result) {
        return result.getAllErrors().isEmpty()
                ? "Donnees invalides."
                : result.getAllErrors().get(0).getDefaultMessage();
    }
}
