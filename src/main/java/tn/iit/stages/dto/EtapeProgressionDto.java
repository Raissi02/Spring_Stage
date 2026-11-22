package tn.iit.stages.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tn.iit.stages.model.StatutEtape;

import java.time.LocalDate;

/**
 * Representation REST d'une etape de progression.
 */
public record EtapeProgressionDto(
        Long id,
        @NotBlank(message = "Le titre est obligatoire") @Size(max = 120) String titre,
        String description,
        @Min(value = 1, message = "L'ordre doit etre superieur ou egal a 1") int ordre,
        StatutEtape statut,
        LocalDate dateValidation,
        @NotNull(message = "L'identifiant du stage est obligatoire") Long stageId,
        Long valideeParId,
        String valideeParNomComplet) {
}
