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
import tn.iit.stages.dto.DtoMapper;
import tn.iit.stages.dto.ObservationDto;
import tn.iit.stages.service.ObservationService;

import java.util.List;

@RestController
@RequestMapping("/api/observations")
public class ObservationRestController {

    private final ObservationService observationService;

    public ObservationRestController(ObservationService observationService) {
        this.observationService = observationService;
    }

    @GetMapping
    public List<ObservationDto> lister(@RequestParam Long stageId) {
        return observationService.findByStage(stageId).stream().map(DtoMapper::toDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ObservationDto creer(@Valid @RequestBody ObservationDto dto) {
        return DtoMapper.toDto(observationService.add(
                dto.stageId(), dto.auteurId(), DtoMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        observationService.delete(id);
    }
}
