package tn.iit.stages.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Representation REST d'un etudiant.
 */
public record EtudiantDto(
        Long id,
        @NotBlank(message = "Le nom est obligatoire") @Size(max = 50) String nom,
        @NotBlank(message = "Le prenom est obligatoire") @Size(max = 50) String prenom,
        @NotBlank(message = "L'email est obligatoire") @Email(message = "Format d'email invalide") String email,
        String telephone,
        String numeroEtudiant,
        String filiere,
        String niveau,
        Long encadrantId,
        String encadrantNomComplet,
        Long stageId) {
}
