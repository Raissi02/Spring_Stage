package tn.iit.stages.service;

import tn.iit.stages.model.Enseignant;

import java.util.List;
import java.util.Optional;

public interface EnseignantService {

    List<Enseignant> findAll();

    List<Enseignant> rechercher(String motCle);

    Optional<Enseignant> findById(Long id);

    Enseignant getById(Long id);

    Enseignant create(Enseignant enseignant);

    Enseignant update(Long id, Enseignant enseignant);

    void delete(Long id);
}
