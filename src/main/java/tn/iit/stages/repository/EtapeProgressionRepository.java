package tn.iit.stages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.iit.stages.model.EtapeProgression;
import tn.iit.stages.model.StatutEtape;

import java.util.List;

public interface EtapeProgressionRepository extends JpaRepository<EtapeProgression, Long> {

    List<EtapeProgression> findByStageIdOrderByOrdreAsc(Long stageId);

    boolean existsByValideeParId(Long enseignantId);

    long countByStatut(StatutEtape statut);
}
