package tn.iit.stages.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tn.iit.stages.model.StatutStage;

import java.time.LocalDate;

/**
 * Representation REST d'un stage.
 */
public record StageDto(
        Long id,
        @NotBlank(message = "Le sujet est obligatoire") @Size(max = 150) String sujet,
        String description,
        @NotNull(message = "La date de debut est obligatoire") LocalDate dateDebut,
        @NotNull(message = "La date de fin est obligatoire") LocalDate dateFin,
        StatutStage statut,
        @NotNull(message = "L'entreprise est obligatoire") Long entrepriseId,
        String entrepriseNom,
        Long etudiantId,
        String etudiantNomComplet) {
}
