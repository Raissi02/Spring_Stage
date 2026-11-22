package tn.iit.stages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.iit.stages.model.StatutStage;
import tn.iit.stages.model.Stage;

import java.util.List;
import java.util.Optional;

public interface StageRepository extends JpaRepository<Stage, Long> {

    List<Stage> findAllByOrderByDateDebutDesc();

    List<Stage> findByStatutOrderByDateDebutDesc(StatutStage statut);

    List<Stage> findByEntrepriseIdOrderByDateDebutDesc(Long entrepriseId);

    List<Stage> findBySujetContainingIgnoreCaseOrderByDateDebutDesc(String sujet);

    Optional<Stage> findFirstByEtudiantId(Long etudiantId);

    boolean existsByEntrepriseId(Long entrepriseId);

    boolean existsByEtudiantId(Long etudiantId);

    long countByStatut(StatutStage statut);

    /**
     * Requete JPQL agregee : nombre de stages par entreprise.
     * Chaque ligne du resultat est un tableau {raisonSociale, total}.
     */
    @Query("select s.entreprise.raisonSociale, count(s) from Stage s "
            + "group by s.entreprise.raisonSociale order by count(s) desc")
    List<Object[]> countStagesParEntreprise();

    @Query("select count(e) from EtapeProgression e where e.stage.id = :stageId")
    long countEtapesByStage(@Param("stageId") Long stageId);
}
