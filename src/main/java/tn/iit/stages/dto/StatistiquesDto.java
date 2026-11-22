package tn.iit.stages.dto;

import java.util.List;
import java.util.Map;

/**
 * Indicateurs du tableau de bord (administrateur pedagogique / API REST).
 */
public record StatistiquesDto(
        long totalEtudiants,
        long totalEnseignants,
        long totalEntreprises,
        long totalStages,
        long etudiantsSansEncadrant,
        long totalEtapes,
        long etapesValidees,
        Map<String, Long> stagesParStatut,
        List<StageParEntreprise> stagesParEntreprise) {

    public record StageParEntreprise(String entreprise, long nombre) {
    }
}
