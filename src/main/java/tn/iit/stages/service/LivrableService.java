package tn.iit.stages.service;

import tn.iit.stages.model.Livrable;

import java.util.List;

public interface LivrableService {

    List<Livrable> findByStage(Long stageId);

    Livrable add(Long stageId, Livrable livrable);

    void delete(Long id);
}
