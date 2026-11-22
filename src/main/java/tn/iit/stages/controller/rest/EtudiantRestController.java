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
import tn.iit.stages.dto.EtudiantDto;
import tn.iit.stages.service.EtudiantService;

import java.util.List;

/**
 * API REST des etudiants (consommee par l'interface CSR).
 */
@RestController
@RequestMapping("/api/etudiants")
public class EtudiantRestController {

    private final EtudiantService etudiantService;

    public EtudiantRestController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @GetMapping
    public List<EtudiantDto> lister(@RequestParam(required = false) String q) {
        return etudiantService.rechercher(q).stream().map(DtoMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public EtudiantDto consulter(@PathVariable Long id) {
        return DtoMapper.toDto(etudiantService.getById(id));
    }

    @GetMapping("/encadrant/{enseignantId}")
    public List<EtudiantDto> listerParEncadrant(@PathVariable Long enseignantId) {
        return etudiantService.findByEncadrant(enseignantId).stream().map(DtoMapper::toDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EtudiantDto creer(@Valid @RequestBody EtudiantDto dto) {
        return DtoMapper.toDto(etudiantService.create(DtoMapper.toEntity(dto), dto.encadrantId()));
    }

    @PutMapping("/{id}")
    public EtudiantDto modifier(@PathVariable Long id, @Valid @RequestBody EtudiantDto dto) {
        return DtoMapper.toDto(etudiantService.update(id, DtoMapper.toEntity(dto), dto.encadrantId()));
    }

    @PutMapping("/{id}/encadrant/{enseignantId}")
    public EtudiantDto affecterEncadrant(@PathVariable Long id, @PathVariable Long enseignantId) {
        return DtoMapper.toDto(etudiantService.affecterEncadrant(id, enseignantId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        etudiantService.delete(id);
    }
}
