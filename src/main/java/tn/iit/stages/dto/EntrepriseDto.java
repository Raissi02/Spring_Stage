package tn.iit.stages.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Representation REST d'une entreprise d'accueil.
 */
public record EntrepriseDto(
        Long id,
        @NotBlank(message = "La raison sociale est obligatoire") @Size(max = 100) String raisonSociale,
        String secteur,
        String contactNom,
        String telephone,
        @Email(message = "Format d'email invalide") String email,
        String adresse,
        int nombreStages) {
}
