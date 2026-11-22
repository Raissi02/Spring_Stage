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
import tn.iit.stages.dto.EtapeProgressionDto;
import tn.iit.stages.service.EtapeProgressionService;

import java.util.List;

@RestController
@RequestMapping("/api/etapes")
public class EtapeRestController {

    private final EtapeProgressionService etapeService;

    public EtapeRestController(EtapeProgressionService etapeService) {
        this.etapeService = etapeService;
    }

    @GetMapping
    public List<EtapeProgressionDto> lister(@RequestParam Long stageId) {
        return etapeService.findByStage(stageId).stream().map(DtoMapper::toDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EtapeProgressionDto creer(@Valid @RequestBody EtapeProgressionDto dto) {
        return DtoMapper.toDto(etapeService.add(dto.stageId(), DtoMapper.toEntity(dto)));
    }

    @PutMapping("/{id}/valider")
    public EtapeProgressionDto valider(@PathVariable Long id,
                                       @RequestParam(required = false) Long enseignantId) {
        return DtoMapper.toDto(etapeService.valider(id, enseignantId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        etapeService.delete(id);
    }
}
