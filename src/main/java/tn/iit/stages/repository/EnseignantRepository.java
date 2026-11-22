package tn.iit.stages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.iit.stages.model.Enseignant;

import java.util.List;
import java.util.Optional;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    Optional<Enseignant> findByEmail(String email);

    List<Enseignant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrSpecialiteContainingIgnoreCase(
            String nom, String prenom, String specialite);
}
