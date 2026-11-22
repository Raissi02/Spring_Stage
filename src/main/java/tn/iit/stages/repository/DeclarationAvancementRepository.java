package tn.iit.stages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.iit.stages.model.DeclarationAvancement;

import java.util.List;

public interface DeclarationAvancementRepository extends JpaRepository<DeclarationAvancement, Long> {

    List<DeclarationAvancement> findByStageIdOrderByDateDeclarationDesc(Long stageId);
}
