package tn.iit.stages.service;

import tn.iit.stages.model.Observation;

import java.util.List;

public interface ObservationService {

    List<Observation> findByStage(Long stageId);

    Observation add(Long stageId, Long auteurId, Observation observation);

    void delete(Long id);
}
