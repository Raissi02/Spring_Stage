package tn.iit.stages.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tn.iit.stages.model.TypeObservation;

import java.time.LocalDate;

/**
 * Representation REST d'une observation ou recommandation.
 */
public record ObservationDto(
        Long id,
        @NotNull(message = "La date de l'observation est obligatoire") LocalDate dateObservation,
        @NotBlank(message = "Le contenu est obligatoire") String contenu,
        TypeObservation type,
        @NotNull(message = "L'identifiant du stage est obligatoire") Long stageId,
        Long auteurId,
        String auteurNomComplet) {
}
