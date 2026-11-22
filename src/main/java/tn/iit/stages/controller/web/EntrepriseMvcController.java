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
import tn.iit.stages.model.Entreprise;
import tn.iit.stages.service.EntrepriseService;

@Controller
@RequestMapping("/entreprises")
public class EntrepriseMvcController {

    private final EntrepriseService entrepriseService;

    public EntrepriseMvcController(EntrepriseService entrepriseService) {
        this.entrepriseService = entrepriseService;
    }

    @InitBinder
    public void interdireLiaisonId(WebDataBinder binder) {
        binder.setDisallowedFields("id");
    }

    @GetMapping
    public String lister(@RequestParam(required = false) String motCle, Model model) {
        model.addAttribute("entreprises", entrepriseService.rechercher(motCle));
        model.addAttribute("motCle", motCle);
        return "entreprises/list";
    }

    @GetMapping("/new")
    public String formulaireCreation(Model model) {
        model.addAttribute("entreprise", new Entreprise());
        return "entreprises/form";
    }

    @PostMapping
    public String creer(@Valid @ModelAttribute("entreprise") Entreprise entreprise,
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "entreprises/form";
        }
        entrepriseService.create(entreprise);
        redirectAttributes.addFlashAttribute("message", "Entreprise ajoutee avec succes.");
        return "redirect:/entreprises";
    }

    @GetMapping("/{id}/edit")
    public String formulaireEdition(@PathVariable Long id, Model model) {
        model.addAttribute("entreprise", entrepriseService.getById(id));
        return "entreprises/form";
    }

    @PostMapping("/{id}")
    public String modifier(@PathVariable Long id,
                           @Valid @ModelAttribute("entreprise") Entreprise entreprise,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "entreprises/form";
        }
        entrepriseService.update(id, entreprise);
        redirectAttributes.addFlashAttribute("message", "Entreprise mise a jour.");
        return "redirect:/entreprises";
    }

    @PostMapping("/{id}/delete")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            entrepriseService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Entreprise supprimee.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/entreprises";
    }
}
