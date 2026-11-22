package tn.iit.stages.controller.rest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.iit.stages.dto.DtoMapper;
import tn.iit.stages.dto.EntrepriseDto;
import tn.iit.stages.service.EntrepriseService;

import java.util.List;

@RestController
@RequestMapping("/api/entreprises")
public class EntrepriseRestController {

    private final EntrepriseService entrepriseService;

    public EntrepriseRestController(EntrepriseService entrepriseService) {
        this.entrepriseService = entrepriseService;
    }

    @GetMapping
    public List<EntrepriseDto> lister(@RequestParam(required = false) String q) {
        return entrepriseService.rechercher(q).stream().map(DtoMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public EntrepriseDto consulter(@PathVariable Long id) {
        return DtoMapper.toDto(entrepriseService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntrepriseDto creer(@Valid @RequestBody EntrepriseDto dto) {
        return DtoMapper.toDto(entrepriseService.create(DtoMapper.toEntity(dto)));
    }

    @PutMapping("/{id}")
    public EntrepriseDto modifier(@PathVariable Long id, @Valid @RequestBody EntrepriseDto dto) {
        return DtoMapper.toDto(entrepriseService.update(id, DtoMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        entrepriseService.delete(id);
    }
}
