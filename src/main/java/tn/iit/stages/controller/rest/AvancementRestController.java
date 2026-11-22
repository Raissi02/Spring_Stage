package tn.iit.stages.controller.rest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.iit.stages.dto.DeclarationAvancementDto;
import tn.iit.stages.dto.DtoMapper;
import tn.iit.stages.service.DeclarationAvancementService;

import java.util.List;

/**
 * API REST des declarations d'avancement etudiantes.
 */
@RestController
@RequestMapping("/api/avancements")
public class AvancementRestController {

    private final DeclarationAvancementService declarationService;

    public AvancementRestController(DeclarationAvancementService declarationService) {
        this.declarationService = declarationService;
    }

    @GetMapping
    public List<DeclarationAvancementDto> lister(@RequestParam Long stageId) {
        return declarationService.findByStage(stageId).stream().map(DtoMapper::toDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeclarationAvancementDto creer(@Valid @RequestBody DeclarationAvancementDto dto) {
        return DtoMapper.toDto(declarationService.add(dto.stageId(), DtoMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        declarationService.delete(id);
    }
}
