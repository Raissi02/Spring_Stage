package tn.iit.stages.service;

import tn.iit.stages.model.Reunion;

import java.util.List;

public interface ReunionService {

    List<Reunion> findByStage(Long stageId);

    Reunion add(Long stageId, Reunion reunion);

    /** Met a jour une reunion existante (objet, date, compte rendu). */
    Reunion update(Long id, Reunion reunion);

    void delete(Long id);
}
