package tn.iit.stages.service;

import tn.iit.stages.dto.StatistiquesDto;
import tn.iit.stages.model.Stage;
import tn.iit.stages.model.StatutStage;

import java.util.List;
import java.util.Optional;

public interface StageService {

    List<Stage> findAll();

    List<Stage> rechercher(String sujet);

    List<Stage> findByStatut(StatutStage statut);

    List<Stage> findByEntreprise(Long entrepriseId);

    Optional<Stage> findById(Long id);

    Stage getById(Long id);

    Stage create(Stage stage, Long entrepriseId, Long etudiantId);

    Stage update(Long id, Stage stage, Long entrepriseId, Long etudiantId);

    void delete(Long id);

    /** Indicateurs agreges pour le tableau de bord et l'API. */
    StatistiquesDto statistiques();
}
