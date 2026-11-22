package tn.iit.stages.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Representation REST d'une reunion de suivi.
 */
public record ReunionDto(
        Long id,
        @NotNull(message = "La date de la reunion est obligatoire") LocalDate dateReunion,
        @NotBlank(message = "L'objet est obligatoire") @Size(max = 150) String objet,
        String compteRendu,
        @NotNull(message = "L'identifiant du stage est obligatoire") Long stageId) {
}
