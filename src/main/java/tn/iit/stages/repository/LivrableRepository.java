package tn.iit.stages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.iit.stages.model.Livrable;

import java.util.List;

public interface LivrableRepository extends JpaRepository<Livrable, Long> {

    List<Livrable> findByStageIdOrderByDateDepotDesc(Long stageId);
}
