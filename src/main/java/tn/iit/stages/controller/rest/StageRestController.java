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
import tn.iit.stages.dto.StageDto;
import tn.iit.stages.model.StatutStage;
import tn.iit.stages.service.StageService;

import java.util.List;

@RestController
@RequestMapping("/api/stages")
public class StageRestController {

    private final StageService stageService;

    public StageRestController(StageService stageService) {
        this.stageService = stageService;
    }

    @GetMapping
    public List<StageDto> lister(@RequestParam(required = false) String q,
                                 @RequestParam(required = false) StatutStage statut) {
        List<tn.iit.stages.model.Stage> stages;
        if (q != null && !q.isBlank()) {
            stages = stageService.rechercher(q);
        } else {
            stages = stageService.findByStatut(statut);
        }
        return stages.stream().map(DtoMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public StageDto consulter(@PathVariable Long id) {
        return DtoMapper.toDto(stageService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StageDto creer(@Valid @RequestBody StageDto dto) {
        return DtoMapper.toDto(stageService.create(
                DtoMapper.toEntity(dto), dto.entrepriseId(), dto.etudiantId()));
    }

    @PutMapping("/{id}")
    public StageDto modifier(@PathVariable Long id, @Valid @RequestBody StageDto dto) {
        return DtoMapper.toDto(stageService.update(
                id, DtoMapper.toEntity(dto), dto.entrepriseId(), dto.etudiantId()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        stageService.delete(id);
    }
}
