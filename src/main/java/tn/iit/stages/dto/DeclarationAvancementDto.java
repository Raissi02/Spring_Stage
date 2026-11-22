package tn.iit.stages.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Representation REST d'une declaration d'avancement etudiante.
 */
public record DeclarationAvancementDto(
        Long id,
        LocalDate dateDeclaration,
        @Min(value = 0, message = "Le pourcentage doit etre compris entre 0 et 100")
        @Max(value = 100, message = "Le pourcentage doit etre compris entre 0 et 100")
        int pourcentage,
        @Size(max = 1000) String commentaire,
        @NotNull(message = "L'identifiant du stage est obligatoire") Long stageId) {
}
