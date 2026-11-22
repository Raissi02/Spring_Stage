package tn.iit.stages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.iit.stages.model.Entreprise;

import java.util.List;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {

    List<Entreprise> findByRaisonSocialeContainingIgnoreCase(String raisonSociale);
}
