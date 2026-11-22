package tn.iit.stages.service;

import tn.iit.stages.model.Entreprise;

import java.util.List;
import java.util.Optional;

public interface EntrepriseService {

    List<Entreprise> findAll();

    List<Entreprise> rechercher(String motCle);

    Optional<Entreprise> findById(Long id);

    Entreprise getById(Long id);

    Entreprise create(Entreprise entreprise);

    Entreprise update(Long id, Entreprise entreprise);

    void delete(Long id);
}
