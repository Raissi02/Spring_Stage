package tn.iit.stages.service;

import tn.iit.stages.model.EtapeProgression;

import java.util.List;

public interface EtapeProgressionService {

    List<EtapeProgression> findByStage(Long stageId);

    EtapeProgression add(Long stageId, EtapeProgression etape);

    /** Valide une etape au nom de l'enseignant encadrant. */
    EtapeProgression valider(Long etapeId, Long enseignantId);

    void delete(Long id);
}
