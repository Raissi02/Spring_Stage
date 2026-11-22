package tn.iit.stages.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import tn.iit.stages.model.Etudiant;
import tn.iit.stages.model.Entreprise;
import tn.iit.stages.model.Stage;
import tn.iit.stages.model.StatutStage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/springJpaStagesJpaTest?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
        "spring.jpa.hibernate.ddl-auto=update"
})
class StageRepositoryTest {

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Test
    void compteLesStagesParStatutEtParEntreprise() {
        Entreprise entreprise = entrepriseRepository.save(new Entreprise(
                "ACME", "Informatique", "Contact", "+216 70 000 000",
                "contact@acme.tn", "Tunis"));

        Etudiant etudiant = etudiantRepository.save(new Etudiant(
                "Ben Salah", "Ahmed", "ahmed@acme.tn", "+216 55 000 000",
                "ETU-1", "GL", "3eme annee"));

        Stage stage = new Stage("Sujet de test", "Description",
                LocalDate.of(2026, 1, 15), LocalDate.of(2026, 6, 15),
                StatutStage.EN_COURS);
        stage.setEntreprise(entreprise);
        stage.setEtudiant(etudiant);
        etudiant.setStage(stage);
        stageRepository.save(stage);

        assertThat(stageRepository.countByStatut(StatutStage.EN_COURS)).isEqualTo(1);
        assertThat(stageRepository.countByStatut(StatutStage.VALIDE)).isZero();
        assertThat(stageRepository.existsByEntrepriseId(entreprise.getId())).isTrue();
        assertThat(stageRepository.existsByEtudiantId(etudiant.getId())).isTrue();
        assertThat(stageRepository.countStagesParEntreprise()).hasSize(1);
    }
}
