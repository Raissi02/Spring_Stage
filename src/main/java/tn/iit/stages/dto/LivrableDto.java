package tn.iit.stages.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Representation REST d'un livrable depose par l'etudiant.
 */
public record LivrableDto(
        Long id,
        @NotBlank(message = "Le titre est obligatoire") @Size(max = 120) String titre,
        @Size(max = 500) String url,
        String description,
        LocalDate dateDepot,
        @NotNull(message = "L'identifiant du stage est obligatoire") Long stageId) {
}
