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
import tn.iit.stages.dto.ReunionDto;
import tn.iit.stages.service.ReunionService;

import java.util.List;

@RestController
@RequestMapping("/api/reunions")
public class ReunionRestController {

    private final ReunionService reunionService;

    public ReunionRestController(ReunionService reunionService) {
        this.reunionService = reunionService;
    }

    @GetMapping
    public List<ReunionDto> lister(@RequestParam Long stageId) {
        return reunionService.findByStage(stageId).stream().map(DtoMapper::toDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReunionDto creer(@Valid @RequestBody ReunionDto dto) {
        return DtoMapper.toDto(reunionService.add(dto.stageId(), DtoMapper.toEntity(dto)));
    }

    @PutMapping("/{id}")
    public ReunionDto modifier(@PathVariable Long id, @Valid @RequestBody ReunionDto dto) {
        return DtoMapper.toDto(reunionService.update(id, DtoMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        reunionService.delete(id);
    }
}
