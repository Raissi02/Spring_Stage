package tn.iit.stages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.iit.stages.model.Reunion;

import java.util.List;

public interface ReunionRepository extends JpaRepository<Reunion, Long> {

    List<Reunion> findByStageIdOrderByDateReunionDesc(Long stageId);
}
