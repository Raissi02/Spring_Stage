package tn.iit.stages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.iit.stages.model.Observation;

import java.util.List;

public interface ObservationRepository extends JpaRepository<Observation, Long> {

    List<Observation> findByStageIdOrderByDateObservationDesc(Long stageId);

    boolean existsByAuteurId(Long enseignantId);
}
