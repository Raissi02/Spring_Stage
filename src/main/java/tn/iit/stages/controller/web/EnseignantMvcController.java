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
import tn.iit.stages.model.Enseignant;
import tn.iit.stages.service.EnseignantService;
import tn.iit.stages.service.EtudiantService;

@Controller
@RequestMapping("/enseignants")
public class EnseignantMvcController {

    private final EnseignantService enseignantService;
    private final EtudiantService etudiantService;

    public EnseignantMvcController(EnseignantService enseignantService,
                                   EtudiantService etudiantService) {
        this.enseignantService = enseignantService;
        this.etudiantService = etudiantService;
    }

    @InitBinder
    public void interdireLiaisonId(WebDataBinder binder) {
        binder.setDisallowedFields("id");
    }

    @GetMapping
    public String lister(@RequestParam(required = false) String motCle, Model model) {
        model.addAttribute("enseignants", enseignantService.rechercher(motCle));
        model.addAttribute("motCle", motCle);
        return "enseignants/list";
    }

    @GetMapping("/new")
    public String formulaireCreation(Model model) {
        model.addAttribute("enseignant", new Enseignant());
        return "enseignants/form";
    }

    @PostMapping
    public String creer(@Valid @ModelAttribute("enseignant") Enseignant enseignant,
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "enseignants/form";
        }
        try {
            enseignantService.create(enseignant);
            redirectAttributes.addFlashAttribute("message", "Enseignant ajoute avec succes.");
            return "redirect:/enseignants";
        } catch (BusinessException ex) {
            result.rejectValue("email", "business", ex.getMessage());
            return "enseignants/form";
        }
    }

    @GetMapping("/{id}")
    public String consulter(@PathVariable Long id, Model model) {
        model.addAttribute("enseignant", enseignantService.getById(id));
        model.addAttribute("etudiants", etudiantService.findByEncadrant(id));
        return "enseignants/details";
    }

    @GetMapping("/{id}/edit")
    public String formulaireEdition(@PathVariable Long id, Model model) {
        model.addAttribute("enseignant", enseignantService.getById(id));
        return "enseignants/form";
    }

    @PostMapping("/{id}")
    public String modifier(@PathVariable Long id,
                           @Valid @ModelAttribute("enseignant") Enseignant enseignant,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "enseignants/form";
        }
        try {
            enseignantService.update(id, enseignant);
            redirectAttributes.addFlashAttribute("message", "Enseignant mis a jour.");
            return "redirect:/enseignants";
        } catch (BusinessException ex) {
            result.rejectValue("email", "business", ex.getMessage());
            return "enseignants/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            enseignantService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Enseignant supprime.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/enseignants";
    }
}
