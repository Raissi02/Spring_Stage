package tn.iit.stages.service;

import tn.iit.stages.model.DeclarationAvancement;

import java.util.List;

public interface DeclarationAvancementService {

    List<DeclarationAvancement> findByStage(Long stageId);

    DeclarationAvancement add(Long stageId, DeclarationAvancement declaration);

    void delete(Long id);
}
