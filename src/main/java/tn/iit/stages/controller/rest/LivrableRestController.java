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
import tn.iit.stages.dto.LivrableDto;
import tn.iit.stages.service.LivrableService;

import java.util.List;

@RestController
@RequestMapping("/api/livrables")
public class LivrableRestController {

    private final LivrableService livrableService;

    public LivrableRestController(LivrableService livrableService) {
        this.livrableService = livrableService;
    }

    @GetMapping
    public List<LivrableDto> lister(@RequestParam Long stageId) {
        return livrableService.findByStage(stageId).stream().map(DtoMapper::toDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivrableDto creer(@Valid @RequestBody LivrableDto dto) {
        return DtoMapper.toDto(livrableService.add(dto.stageId(), DtoMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        livrableService.delete(id);
    }
}
