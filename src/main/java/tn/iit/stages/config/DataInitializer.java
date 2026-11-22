package tn.iit.stages.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.iit.stages.model.DeclarationAvancement;
import tn.iit.stages.model.EtapeProgression;
import tn.iit.stages.model.Etudiant;
import tn.iit.stages.model.Enseignant;
import tn.iit.stages.model.Entreprise;
import tn.iit.stages.model.Livrable;
import tn.iit.stages.model.Observation;
import tn.iit.stages.model.Reunion;
import tn.iit.stages.model.StatutEtape;
import tn.iit.stages.model.StatutStage;
import tn.iit.stages.model.Stage;
import tn.iit.stages.model.TypeObservation;
import tn.iit.stages.repository.EnseignantRepository;
import tn.iit.stages.repository.EntrepriseRepository;
import tn.iit.stages.repository.EtudiantRepository;
import tn.iit.stages.repository.StageRepository;

import java.time.LocalDate;

/**
 * Jeu de donnees de demonstration (execute une seule fois, si la base est vide).
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner chargerDonneesDemo(EtudiantRepository etudiantRepository,
                                         EnseignantRepository enseignantRepository,
                                         EntrepriseRepository entrepriseRepository,
                                         StageRepository stageRepository) {
        return args -> {
            if (stageRepository.count() > 0) {
                return;
            }

            Entreprise vermeg = entrepriseRepository.save(new Entreprise(
                    "Vermeg", "Edition de logiciels financiers", "Salma Ben Ali",
                    "+216 71 000 001", "contact@vermeg.tn", "Lac 2, Tunis"));
            Entreprise sagemcom = entrepriseRepository.save(new Entreprise(
                    "Sagemcom", "Systemes de communication", "Karim Trabelsi",
                    "+216 71 000 002", "contact@sagemcom.tn", "Z.I. Charguia, Tunis"));
            Entreprise telnet = entrepriseRepository.save(new Entreprise(
                    "Telnet", "Ingenierie et conseil", "Nizar Gharbi",
                    "+216 74 000 003", "contact@telnet.tn", "Technopole, Sfax"));

            Enseignant zayani = enseignantRepository.save(new Enseignant(
                    "Zayani", "Mohamed", "mohamed.zayani@iit.tn", "+216 98 111 222",
                    "Maitre de conferences", "Genie logiciel"));
            Enseignant trabelsi = enseignantRepository.save(new Enseignant(
                    "Trabelsi", "Ines", "ines.trabelsi@iit.tn", "+216 98 333 444",
                    "Assistante", "Architectures logicielles"));
            Enseignant benAmor = enseignantRepository.save(new Enseignant(
                    "Ben Amor", "Sami", "sami.benamor@iit.tn", "+216 98 555 666",
                    "Professeur", "Bases de donnees"));

            Etudiant ahmed = new Etudiant("Ben Salah", "Ahmed", "ahmed.bensalah@iit.tn",
                    "+216 55 100 200", "ETU-2026-001", "Genie Logiciel", "3eme annee");
            ahmed.setEncadrant(zayani);
            ahmed = etudiantRepository.save(ahmed);

            Etudiant mariem = new Etudiant("Gharbi", "Mariem", "mariem.gharbi@iit.tn",
                    "+216 55 100 201", "ETU-2026-002", "Genie Logiciel", "3eme annee");
            mariem.setEncadrant(trabelsi);
            mariem = etudiantRepository.save(mariem);

            Etudiant youssef = new Etudiant("Khelifi", "Youssef", "youssef.khelifi@iit.tn",
                    "+216 55 100 202", "ETU-2026-003", "Data Science", "2eme annee");
            youssef.setEncadrant(benAmor);
            youssef = etudiantRepository.save(youssef);

            Etudiant nour = new Etudiant("Mansouri", "Nour", "nour.mansouri@iit.tn",
                    "+216 55 100 203", "ETU-2026-004", "Genie Logiciel", "2eme annee");
            nour.setEncadrant(zayani);
            etudiantRepository.save(nour);

            etudiantRepository.save(new Etudiant("Chaabane", "Walid", "walid.chaabane@iit.tn",
                    "+216 55 100 204", "ETU-2026-005", "Data Science", "3eme annee"));

            /* ---------------- Stage 1 : en cours ---------------- */
            Stage apiCredits = new Stage(
                    "Developpement d'une API de gestion des credits",
                    "Conception et implementation d'une API REST pour la gestion des credits bancaires "
                            + "avec Spring Boot et tests automatises.",
                    LocalDate.now().minusWeeks(4), LocalDate.now().plusWeeks(20),
                    StatutStage.EN_COURS);
            apiCredits.setEntreprise(vermeg);
            apiCredits.setEtudiant(ahmed);
            ahmed.setStage(apiCredits);

            EtapeProgression analyse = new EtapeProgression("Cadrage et analyse du besoin",
                    "Entretiens avec le metier et redaction du cahier des charges.", 1);
            analyse.setStatut(StatutEtape.VALIDEE);
            analyse.setDateValidation(LocalDate.now().minusWeeks(2));
            analyse.setValideePar(zayani);
            apiCredits.addEtape(analyse);

            apiCredits.addEtape(new EtapeProgression("Conception de l'API",
                    "Modelisation du domaine et definition des endpoints REST.", 2));

            EtapeProgression developpement = new EtapeProgression("Developpement",
                    "Implementation des services, de la persistance et des tests.", 3);
            developpement.setStatut(StatutEtape.EN_COURS);
            apiCredits.addEtape(developpement);

            apiCredits.addReunion(new Reunion(LocalDate.now().minusWeeks(3),
                    "Point de suivi n.1",
                    "Avancement conforme au planning. Le cahier des charges est valide."));
            apiCredits.addReunion(new Reunion(LocalDate.now().minusWeeks(1),
                    "Point de suivi n.2",
                    "Premiere version de l'API disponible. Poursuivre les tests d'integration."));

            Observation observation1 = new Observation(LocalDate.now().minusDays(6),
                    "Bonne maitrise du framework. Penser a documenter les endpoints.",
                    TypeObservation.OBSERVATION);
            observation1.setAuteur(zayani);
            apiCredits.addObservation(observation1);

            Observation recommandation1 = new Observation(LocalDate.now().minusDays(3),
                    "Ajouter une couverture de tests superieure a 80% avant la livraison.",
                    TypeObservation.RECOMMANDATION);
            recommandation1.setAuteur(zayani);
            apiCredits.addObservation(recommandation1);

            apiCredits.addLivrable(new Livrable("Depot Git de l'API",
                    "https://github.com/example/stage-api-credits",
                    "Code source de la premiere version.", LocalDate.now().minusDays(4)));

            apiCredits.addDeclaration(new DeclarationAvancement(
                    LocalDate.now().minusDays(12), 30,
                    "Cadrage termine, demarrage de la conception."));
            apiCredits.addDeclaration(new DeclarationAvancement(
                    LocalDate.now().minusDays(5), 55,
                    "Conception validee, developpement et tests en cours."));

            stageRepository.save(apiCredits);

            /* ---------------- Stage 2 : en attente ---------------- */
            Stage supervision = new Stage(
                    "Plateforme de supervision des equipements reseau",
                    "Mise en place d'un tableau de bord de supervision temps reel.",
                    LocalDate.now().plusWeeks(2), LocalDate.now().plusMonths(5),
                    StatutStage.EN_ATTENTE);
            supervision.setEntreprise(sagemcom);
            supervision.setEtudiant(mariem);
            mariem.setStage(supervision);

            supervision.addEtape(new EtapeProgression("Etude de l'existant",
                    "Analyse des outils de supervision utilises.", 1));
            supervision.addEtape(new EtapeProgression("Prototype",
                    "Maquette du tableau de bord et choix technologiques.", 2));

            stageRepository.save(supervision);

            /* ---------------- Stage 3 : termine et valide ---------------- */
            Stage dataPlatform = new Stage(
                    "Mise en place d'une plateforme de donnees",
                    "Construction d'un entrepot de donnees et de rapports analytiques.",
                    LocalDate.now().minusMonths(6), LocalDate.now().minusDays(10),
                    StatutStage.VALIDE);
            dataPlatform.setEntreprise(telnet);
            dataPlatform.setEtudiant(youssef);
            youssef.setStage(dataPlatform);

            EtapeProgression etape1 = new EtapeProgression("Collecte des donnees",
                    "Identification des sources et extraction initiale.", 1);
            etape1.setStatut(StatutEtape.VALIDEE);
            etape1.setDateValidation(LocalDate.now().minusMonths(4));
            etape1.setValideePar(benAmor);
            dataPlatform.addEtape(etape1);

            EtapeProgression etape2 = new EtapeProgression("Modelisation dimensionnelle",
                    "Conception du schema en etoile.", 2);
            etape2.setStatut(StatutEtape.VALIDEE);
            etape2.setDateValidation(LocalDate.now().minusMonths(2));
            etape2.setValideePar(benAmor);
            dataPlatform.addEtape(etape2);

            EtapeProgression etape3 = new EtapeProgression("Rapports et soutenance",
                    "Tableaux de bord et rapport final.", 3);
            etape3.setStatut(StatutEtape.VALIDEE);
            etape3.setDateValidation(LocalDate.now().minusDays(15));
            etape3.setValideePar(benAmor);
            dataPlatform.addEtape(etape3);

            Observation bilan = new Observation(LocalDate.now().minusDays(20),
                    "Travail de grande qualite, livrables complets et dans les delais.",
                    TypeObservation.OBSERVATION);
            bilan.setAuteur(benAmor);
            dataPlatform.addObservation(bilan);

            dataPlatform.addLivrable(new Livrable("Rapport final",
                    "https://example.com/rapport-final.pdf",
                    "Rapport de stage complet.", LocalDate.now().minusDays(25)));
            dataPlatform.addLivrable(new Livrable("Depot Git",
                    "https://github.com/example/data-platform",
                    "Code source de la plateforme.", LocalDate.now().minusDays(30)));

            dataPlatform.addDeclaration(new DeclarationAvancement(
                    LocalDate.now().minusDays(18), 100,
                    "Tous les travaux et livrables ont ete finalises."));

            stageRepository.save(dataPlatform);
        };
    }
}
