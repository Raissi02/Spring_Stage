# Gestion des stages et du suivi des etudiants

Mini-projet du module **Architectures logicielles evoluees : Framework Spring**
(IIT - A.U. 2026/2027).

Application Spring Boot de gestion des stages universitaires : etudiants, enseignants
encadrants, entreprises d'accueil et suivi complet du stage (reunions, observations,
etapes de progression, livrables, declarations d'avancement).

- Interface **SSR** : Spring MVC + Thymeleaf
- Interface **CSR** : API REST + page JavaScript (`fetch`)
- Persistance : Spring Data JPA / Hibernate sur **MySQL (XAMPP)** en local,
  MySQL 8.4 sous Docker
- Deploiement : Docker + Docker Compose

> Etat du projet : code complet, executable et teste (13 tests automatises).
> Validation du 2026-11-22 consignee en [section 13](#13-journal-de-validation).
> Restent a produire : rapport UML (15-20 pages), video de demonstration et depot
> GitHub avec branches et Pull Requests.

## 0. Demarrage rapide

Prerequis deja installes sur ce poste : **JDK 21**, **Maven 3.9.11** et
**XAMPP** dans `D:\xampp`. Sinon, voir [section 4](#4-prerequis).

1. **Demarrer MySQL** : lancer `D:\xampp\xampp-control.exe` puis cliquer sur
   **Start** en face de **MySQL** (ou executer `D:\xampp\mysql_start.bat`).
   Verification optionnelle :

   ```powershell
   D:\xampp\mysql\bin\mysql.exe -u root -e "SELECT VERSION();"
   ```

2. **Ouvrir un nouveau terminal** (pour charger `JAVA_HOME` / `MAVEN_HOME`) a la
   racine du projet :

   ```powershell
   cd "D:\Mini Project\stage-management"
   ```

3. **Verifier l'environnement** :

   ```powershell
   java -version     # doit afficher la version 21
   mvn -v            # Apache Maven 3.9.11 + Java 21
   ```

4. **Lancer l'application** :

   ```powershell
   mvn spring-boot:run
   ```

5. **Attendre** la ligne `Started StageManagementApplication in ... seconds`,
   puis ouvrir <http://localhost:8080>.
   Au premier demarrage, la base `springJpaStages` est creee et remplie avec les
   donnees de demonstration ; les demarrages suivants conservent ces donnees.

6. **Verifier que l'application repond** :
   `/`, `/admin`, `/enseignant`, `/etudiant`, `/etudiants/1`, `/stages/1`,
   `/csr/index.html`, `/api/statistiques`.

7. **Arreter** l'application avec `Ctrl+C` dans le terminal.

| Besoin | Commande |
|---|---|
| Tests automatises (MySQL demarre) | `mvn test` |
| Construire et executer le jar | `mvn clean package` puis `java -jar target\stage-management-0.0.1-SNAPSHOT.jar` |
| Docker (sans XAMPP) | `docker compose up --build` |
| Reinitialiser les donnees de demo | `D:\xampp\mysql\bin\mysql.exe -u root -e "DROP DATABASE IF EXISTS springJpaStages;"` puis relancer |

Les details (installation du JDK, de Maven et de MySQL, variantes d'execution,
diagnostic des erreurs) sont dans les sections [4](#4-prerequis) et
[5](#5-execution-de-lapplication).

---

## Sommaire

0. [Demarrage rapide](#0-demarrage-rapide)
1. [Presentation fonctionnelle](#1-presentation-fonctionnelle)
2. [Technologies utilisees](#2-technologies-utilisees)
3. [Architecture](#3-architecture)
4. [Prerequis](#4-prerequis)
5. [Execution de l'application](#5-execution-de-lapplication)
6. [URLs et donnees de demonstration](#6-urls-et-donnees-de-demonstration)
7. [API REST](#7-api-rest)
8. [Tests](#8-tests)
9. [Couverture du cahier des charges](#9-couverture-du-cahier-des-charges)
10. [Organisation de l'equipe](#10-organisation-de-lequipe)
11. [Workflow Git/GitHub](#11-workflow-gitgithub)
12. [Limites et perspectives](#12-limites-et-perspectives)
13. [Journal de validation](#13-journal-de-validation)

---

## 1. Presentation fonctionnelle

Trois espaces explicites sont proposes des la page d'accueil (`/`) :
**administration** (`/admin`), **enseignant** (`/enseignant`) et **etudiant**
(`/etudiant`). L'authentification n'est pas demandee par le sujet (voir
[Limites](#12-limites-et-perspectives)) : chaque espace permet de choisir le
profil (enseignant ou etudiant) avant d'acceder a ses donnees.

### Administrateur pedagogique

- Gerer les etudiants et les enseignants (CRUD complet)
- Gerer les entreprises d'accueil (CRUD complet)
- Affecter un enseignant encadrant a un etudiant
- Consulter les stages et leurs statistiques (tableau de bord)

### Enseignant encadrant

- Consulter ses etudiants affectes et l'etat de leur stage
- Gerer les reunions de suivi (ajout, suppression en SSR ; CRUD complet en REST)
- Ajouter des observations et des recommandations
- Valider les etapes de progression du stage

### Etudiant stagiaire

- Consulter les informations de son stage (entreprise, periode, statut, reunions, etapes)
- Declarer l'avancement de ses travaux (pourcentage + commentaire)
- Deposer des livrables (titre, lien, description)
- Consulter les observations de son encadrant

---

## 2. Technologies utilisees

### 2.1 Vue d'ensemble

| Domaine | Technologie | Version | Role dans le projet |
|---|---|---|---|
| Langage | Java | 21 (LTS) | Langage de developpement |
| Build | Maven | 3.9.11 (installe) | Compilation, dependances, packaging |
| Framework | Spring Boot | 3.5.5 | Auto-configuration, starters, serveur embarque |
| Conteneur IoC | Spring Framework | alignee par le BOM | Injection de dependances, cycle de vie des beans |
| Web SSR | Spring MVC | alignee par le BOM | Controleurs, formulaires, navigation |
| Templates | Thymeleaf + Layout Dialect | 3.1.x (gere) | Rendu HTML cote serveur |
| Persistance | Spring Data JPA + Hibernate | gere par le BOM | Mapping objet-relationnel, repositories |
| Base de donnees | MySQL / MariaDB (XAMPP), MySQL 8.4 (Docker) | 10.4.x (XAMPP) / 8.4 | Persistance des donnees |
| Driver JDBC | MySQL Connector/J | 9.4.0 (gere) | Connexion JDBC (compatible MariaDB de XAMPP) |
| Validation | Jakarta Bean Validation + Hibernate Validator | gere par le BOM | Contraintes sur entites et DTO |
| JSON | Jackson | gere par le BOM | Serialisation/deserialisation REST |
| Serveur web | Apache Tomcat embarque | 10.1.x (gere) | Conteneur de servlets integre |
| UI | Bootstrap + CSS personnalise | 5.3.3 (CDN) | Mise en page responsive |
| CSR | JavaScript ES6 (`fetch`) | - | Appels HTTP vers l'API REST |
| Tests | JUnit 5, AssertJ, Mockito, MockMvc | geres par le BOM | Tests unitaires, slices et integration |
| Conteneurisation | Docker, Docker Compose | - | Image applicative + MySQL |
| Versionnement | Git / GitHub | - | Collaboration (a initialiser) |

> "Gere par le BOM" signifie que la version est alignee automatiquement par
> `spring-boot-starter-parent` 3.5.5 : aucune version explicite n'est necessaire
> dans le `pom.xml`.

### 2.2 Detail par technologie

#### Java 21

- **Role** : langage de l'application.
- **Utilisation** : `record` pour les DTO REST (`EtudiantDto`, `StageDto`, ...),
  `java.time.LocalDate` pour les dates, streams pour les conversions et calculs
  (`getAvancement()`), `Optional` dans les services.
- **Pourquoi** : version LTS, compatible avec Spring Boot 3.x (qui exige Java 17+),
  et imposee par le sujet (JDK 21 dans les ateliers Maven).

#### Maven 3.9.11

- **Role** : gestion des dependances et du cycle de vie du build.
- **Utilisation** : `pom.xml` (parent Spring Boot, starters, plugins) et commandes
  `mvn clean`, `mvn compile`, `mvn test`, `mvn package`, `mvn spring-boot:run`.
- **Installation** : identique a l'atelier Maven (Partie 01) : archive binaire
  extraite dans `D:\Dev\Outils\Maven`, variables `MAVEN_HOME` et `Path`
  configurees (voir 4.3).
- **Depot local** : `C:\Users\<utilisateur>\.m2\repository` (emplacement par
  defaut presente dans l'atelier ; personnalisation possible via `settings.xml`).
- **Pourquoi** : outil de build standard du monde JEE/Spring, installe et
  configure manuellement comme dans le cours (`MAVEN_HOME` + `Path`).

#### Spring Boot 3.5.5

- **Role** : socle applicatif.
- **Utilisation** :
  - `@SpringBootApplication` dans `StageManagementApplication` ;
  - starters : `web`, `thymeleaf`, `data-jpa`, `validation`, `test` ;
  - auto-configuration de Tomcat, Hibernate, Jackson, Thymeleaf ;
  - profils de configuration (`application.properties` pour XAMPP,
    `application-docker.properties` pour Docker) ;
  - `spring-boot-devtools` pour le rechargement en developpement ;
  - plugin `spring-boot-maven-plugin` pour produire un jar executable.
- **Pourquoi** : convention over configuration, serveur embarque, demarrage rapide,
  ecosysteme complet pour MVC + REST + JPA.

#### Spring IoC / Dependency Injection

- **Role** : assemblage des composants et inversion de controle.
- **Utilisation** : injection par constructeur dans les services et controleurs ;
  stereotypes `@Service`, `@Repository`, `@RestController`, `@Controller`,
  `@Configuration` ; `@Bean` pour le jeu de donnees (`DataInitializer`).
- **Pourquoi** : faible couplage, testabilite (Mockito injecte des mocks),
  pas de `new` disperses dans le code.

#### Spring MVC

- **Role** : couche web SSR et REST.
- **Utilisation** :
  - `@Controller` + `@RequestMapping` + `Model` pour les vues Thymeleaf ;
  - `@ModelAttribute`, `@Valid`, `BindingResult` pour les formulaires ;
  - `@InitBinder` pour interdire la liaison du parametre d'URL `{id}` aux entites
    enfants (protection contre un bug de binding) ;
  - `RedirectAttributes` pour les messages flash (succes/erreur) ;
  - `@RestController`, `@RequestBody`, `@PathVariable`, `@RequestParam`,
    `@ResponseStatus` pour l'API JSON.
- **Pourquoi** : un seul framework pour SSR et CSR, integration native avec
  Thymeleaf et la validation.

#### Thymeleaf 3.1 + Layout Dialect

- **Role** : moteur de template SSR.
- **Utilisation** :
  - `layout.html` (navbar + alertes + footer) et `layout:decorate` dans chaque vue ;
  - attributs `th:each`, `th:text`, `th:if`, `th:href`, `th:field`, `th:object`,
    `th:errors`, `th:classappend` ;
  - expressions de lien `@{...}` pour les URLs contextuelles.
- **Pourquoi** : templates HTML naturels (ouvrables dans un navigateur),
  integration Spring, protection XSS par defaut (`th:text` echappe le HTML).

#### Spring Data JPA + Hibernate

- **Role** : persistance.
- **Utilisation** :
  - entites : `@Entity`, `@Id`, `@GeneratedValue`, `@Column`, `@Enumerated`,
    `@MappedSuperclass` (`Personne`), relations `@OneToOne`, `@ManyToOne`,
    `@OneToMany` avec `cascade` et `orphanRemoval` ;
  - repositories : `JpaRepository` + requetes derivees
    (`findByEncadrantId`, `findByNomContainingIgnoreCaseOr...`, `countByStatut`) ;
  - requete JPQL agregee pour les statistiques
    (`countStagesParEntreprise`, `@Query`) ;
  - tri via `@OrderBy` sur les collections.
- **Pourquoi** : suppression du code DAO repetitif, mapping objet-relationnel
  standard, requetes typees et verifiees au demarrage.

#### MySQL / MariaDB (XAMPP)

- **Role** : base de donnees locale de developpement et base des tests.
- **Utilisation** :
  - XAMPP fournit **MariaDB 10.4** (compatible MySQL), demarre sur `localhost:3306` ;
  - base applicative `springJpaStages`, creee automatiquement
    (`createDatabaseIfNotExist=true`) ;
  - schema cree/mis a jour par Hibernate (`ddl-auto=update`) ;
  - base `stages` (service MySQL 8.4) pour le profil Docker ;
  - bases dediees aux tests : `springJpaStagesTest` et `springJpaStagesJpaTest`.
  - driver `com.mysql:mysql-connector-j` (9.4.0, gere par le BOM), verifie
    avec la MariaDB de XAMPP 10.4.32.
- **Pourquoi** : MySQL est la base utilisee dans les ateliers du module
  (XAMPP + phpMyAdmin) ; les donnees sont persistantes entre deux demarrages.

#### Jakarta Bean Validation

- **Role** : validation des donnees.
- **Utilisation** : `@NotBlank`, `@Email`, `@Size`, `@NotNull`, `@Min`, `@Max`
  sur les entites (formulaires SSR) et les DTO (API REST) ; `@Valid` declenche
  la validation ; `BindingResult` recupere les erreurs ; les messages sont
  affiches via `th:errors` ou renvoyes en JSON.
- **Pourquoi** : regles declaratives centralisees, messages coherents,
  double protection (client + serveur).

#### Jackson / REST + ProblemDetail

- **Role** : contrats JSON et gestion d'erreurs.
- **Utilisation** : serialisation automatique des DTO (`record`), dates ISO
  (`LocalDate`), `ProblemDetail` (RFC 7807) dans `ApiExceptionHandler` pour
  `400` (validation, parametre manquant/invalide), `404` (ressource absente)
  et `409` (regle metier).
- **Pourquoi** : contrats stables, pas d'exposition directe des entites JPA,
  erreurs exploitables par un client CSR.

#### Tomcat embarque

- **Role** : serveur HTTP.
- **Utilisation** : fourni par `spring-boot-starter-web`, demarre sur le port 8080.
- **Pourquoi** : aucun serveur externe a installer ou deployer (contrairement
  aux ateliers Maven/Tomcat).

#### Bootstrap 5.3.3 + CSS

- **Role** : interface utilisateur.
- **Utilisation** : CDN dans `layout.html` et la page CSR ; cartes, tableaux,
  formulaires, badges, barres de progression ; `app.css` pour les couleurs de
  statut (`statut-EN_COURS`, `etape-VALIDEE`, etc.).
- **Pourquoi** : rendu professionnel et responsive sans toolchain front-end.

#### JavaScript `fetch` (CSR)

- **Role** : interface CSR legere.
- **Utilisation** : `src/main/resources/static/csr/index.html` appelle
  `/api/statistiques`, `/api/etudiants`, `/api/stages` et cree un etudiant via
  `POST /api/etudiants`.
- **Pourquoi** : demontre la consommation de l'API REST sans dependance
  (pas de Node.js, pas de build front) ; choix libre autorise par le sujet.

#### JUnit 5, AssertJ, Mockito, MockMvc

- **Role** : tests automatises.
- **Utilisation** : tests unitaires (Mockito), tests de repositories
  (`@DataJpaTest`), tests de controleurs REST (`@WebMvcTest` + MockMvc),
  tests d'integration MVC (`@SpringBootTest` + `@AutoConfigureMockMvc`),
  assertions fluides (AssertJ).
- **Pourquoi** : verrouiller les regles metier et les contrats HTTP, detecter
  les regressions (deux bugs reels ont ete interceptes par ces tests).

#### Docker + Docker Compose

- **Role** : conteneurisation et deploiement.
- **Utilisation** : `Dockerfile` multi-stage (build Maven/JDK 21 puis runtime JRE 21)
  et `docker-compose.yml` (MySQL 8.4 avec healthcheck + application).
- **Pourquoi** : environnement reproductible, demarrage en une commande,
  separation application / base. Docker embarque son propre MySQL : XAMPP
  n'est pas necessaire dans ce mode.

#### Git / GitHub

- **Role** : collaboration a trois (branches, Pull Requests).
- **Utilisation** : `.gitignore` et `.gitattributes` fournis ; strategie de
  branches documentee en [section 11](#11-workflow-gitgithub). Le depot distant
  reste a initialiser par l'equipe.

---

## 3. Architecture

### 3.1 Decoupage en couches

| Couche | Package | Responsabilite | Exemples |
|---|---|---|---|
| Presentation SSR | `controller.web` + `templates/` | Requetes HTTP, binding, modele, vues | `EtudiantMvcController`, `stages/details.html` |
| Presentation REST | `controller.rest` + `dto` | Endpoints JSON, contrats DTO | `StageRestController`, `StageDto` |
| Metier | `service` + `service.impl` | Regles metier, transactions | `StageServiceImpl`, `DeclarationAvancementServiceImpl` |
| Persistance | `repository` | Acces aux donnees, requetes | `StageRepository`, `EtudiantRepository` |
| Domaine | `model` | Entites JPA, enums, invariants simples | `Stage`, `StatutStage` |
| Transverse | `exception`, `config` | Erreurs centralisees, donnees de demo | `ApiExceptionHandler`, `DataInitializer` |

Aucune couche ne remonte vers la presentation : les controleurs appellent les
services, les services utilisent les repositories, les entites ne connaissent
pas le web.

### 3.2 Arborescence

```
stage-management/
|-- pom.xml
|-- Dockerfile
|-- docker-compose.yml
|-- README.md
|-- src/main/java/tn/iit/stages/
|   |-- StageManagementApplication.java
|   |-- model/            Personne, Etudiant, Enseignant, Entreprise, Stage,
|   |                     Reunion, Observation, EtapeProgression, Livrable,
|   |                     DeclarationAvancement + enums
|   |-- repository/       Interfaces Spring Data
|   |-- service/          Contrats metier
|   |   `-- impl/         Implementations @Transactional
|   |-- dto/              Records REST + DtoMapper
|   |-- controller/
|   |   |-- web/          Controleurs MVC (SSR)
|   |   `-- rest/         Controleurs REST (CSR)
|   |-- exception/        ResourceNotFound, Business, ApiExceptionHandler
|   `-- config/           DataInitializer
|-- src/main/resources/
|   |-- application.properties          (XAMPP / MySQL)
|   |-- application-docker.properties   (Docker)
|   |-- templates/        layout + 17 vues Thymeleaf
|   `-- static/
|       |-- css/app.css
|       `-- csr/index.html
`-- src/test/
    |-- resources/application-test.properties   (MySQL de test)
    `-- java/tn/iit/stages/
        |-- StageManagementApplicationTests.java
        |-- repository/StageRepositoryTest.java
        |-- service/EtudiantServiceTest.java
        |-- service/DeclarationAvancementServiceTest.java
        |-- controller/rest/StageRestControllerTest.java
        |-- controller/web/StageMvcControllerTest.java
        `-- controller/web/EtudiantMvcControllerTest.java
```

### 3.3 Flux d'une requete

**Scenario SSR** (ex. afficher la fiche d'un stage) :

```
Navigateur
  -> EtudiantMvcController / StageMvcController
  -> Service (transaction, regles)
  -> Repository (JPA)
  -> Hibernate -> base MySQL (XAMPP)
  <- Modele (Model)
  <- vue Thymeleaf (layout + fragment)
  <- HTML
```

**Scenario CSR** (ex. creer un etudiant depuis la page `/csr/index.html`) :

```
Page JavaScript
  -> fetch POST /api/etudiants (JSON)
  -> EtudiantRestController (@Valid EtudiantDto)
  -> EtudiantService (regles metier)
  -> Repository -> base MySQL
  <- DTO JSON (201 Created) ou ProblemDetail (400/409)
  -> mise a jour du DOM
```

### 3.4 Modele de domaine

| Relation | Implementation JPA |
|---|---|
| `Etudiant` / `Enseignant` heritent de `Personne` | `@MappedSuperclass` (colonnes dupliquees) |
| `Entreprise` 1-N `Stage` | `@ManyToOne` cote `Stage` |
| `Etudiant` 1-1 `Stage` | `@OneToOne` + colonne `etudiant_id` unique |
| `Enseignant` 1-N `Etudiant` (encadrant) | `@ManyToOne` cote `Etudiant` |
| `Stage` 1-N `Reunion`, `Observation`, `EtapeProgression`, `Livrable`, `DeclarationAvancement` | `@OneToMany(cascade = ALL, orphanRemoval = true)` |

Regles metier principales (couche service) :

- email unique entre etudiants et enseignants ;
- un etudiant ne peut avoir qu'un seul stage ;
- date de fin strictement posterieure a la date de debut ;
- suppression d'une entreprise impossible si des stages y sont rattaches ;
- suppression d'un enseignant impossible s'il encadre encore des etudiants ou
  a redige/valide des elements ;
- pourcentage d'avancement declare entre 0 et 100 ;
- validation d'une etape : statut `VALIDEE`, date et validateur renseignes.

### 3.5 Choix de conception

- **Couche service systematique** : les controleurs ne manipulent jamais les
  repositories directement (testabilite, transactions, regles centralisees).
- **DTO pour le REST** : pas d'exposition des entites JPA (pas de cycles
  bidirectionnels, pas de fuite de champs internes), contrats explicites.
- **Double validation** : entites pour les formulaires SSR, DTO pour l'API ;
  les deux s'appuient sur Jakarta Bean Validation.
- **Erreurs normalisees** : `ProblemDetail` (RFC 7807) pour l'API, page
  `error.html` pour le SSR.
- **`@InitBinder`** : le parametre d'URL `{id}` (identifiant du stage) ne doit
  pas etre lie a l'attribut `id` des entites enfants ; sinon Hibernate recoit
  une entite "detachee". Un test de non-regression couvre ce point.
- **Dialecte Hibernate** : detection automatique du moteur (MariaDB de XAMPP ou
  MySQL 8.4 sous Docker), pas de dialecte fige dans le profil par defaut.
- **`spring.jpa.open-in-view=true`** : necessaire pour afficher les collections
  paresseuses dans Thymeleaf ; une evolution possible est de passer a des
  projections/DTO ou des fetch joins.
- **Pas de Lombok** : getters/setters explicites (objectif pedagogique, pas de
  traitement d'annotations supplementaire).

---

## 4. Prerequis

| Outil | Obligatoire | Version | Verification |
|---|---|---|---|
| JDK | Oui | 21 | `java -version` |
| MySQL (XAMPP) | Oui (application et tests) | MariaDB 10.4.x | `D:\xampp\mysql\bin\mysql.exe -u root -e "SELECT VERSION();"` |
| Maven | Oui | 3.9.11 | `mvn -v` |
| Docker Desktop | Non (uniquement pour Docker) | recente | `docker --version` |
| Connexion Internet | Oui au premier lancement | - | telechargement Maven/dependances |

> XAMPP est installe dans `D:\xampp`. Le module MySQL doit etre demarre avant
> de lancer l'application **et** avant de lancer les tests.

### 4.1 Installer et configurer le JDK 21

Telecharger un JDK 21 (Temurin, Oracle ou Microsoft OpenJDK), puis configurer
`JAVA_HOME`.

Sur Windows (interface graphique) :

1. Installer le JDK (ex. `C:\Program Files\Eclipse Adoptium\jdk-21...`).
2. Ouvrir "Variables d'environnement" (Recherche Windows > "variables").
3. Creer une variable systeme `JAVA_HOME` = chemin du JDK (sans `\bin`).
4. Ajouter `%JAVA_HOME%\bin` en tete de la variable `Path`.
5. Ouvrir un nouveau terminal et verifier `java -version`.

En PowerShell (session courante, sans redemarrage) :

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.x-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
java -version
```

> Remarque pour ce poste : un JDK 21 portable est installe dans
> `C:\Users\ahmedr2\tools\jdk21`. Les commandes de ce README fonctionnent en
> definissant `JAVA_HOME` sur ce dossier. Pour un poste standard, remplacer par
> le chemin d'installation du JDK.

### 4.2 Demarrer MySQL avec XAMPP

XAMPP est installe dans `D:\xampp` et fournit MariaDB (compatible MySQL).

**Option interface graphique**

1. Lancer `D:\xampp\xampp-control.exe`.
2. Cliquer sur **Start** en face du module **MySQL** (port `3306`).

**Option console**

```powershell
D:\xampp\mysql_start.bat
```

Garder la fenetre ouverte tant que l'application tourne (la fermer arrete MySQL).

**Verifier la connexion**

```powershell
D:\xampp\mysql\bin\mysql.exe -u root -e "SELECT VERSION();"
```

Identifiants par defaut de XAMPP : utilisateur `root`, mot de passe vide.
Si vous avez defini un mot de passe, mettez-le a jour dans
`src/main/resources/application.properties` (`spring.datasource.password`).

**Arreter MySQL**

```powershell
D:\xampp\mysql_stop.bat
```

ou le bouton **Stop** de XAMPP Control Panel.

### 4.3 Installer et configurer Maven 3.9.11

Procedure identique a l'atelier **Maven - Partie 01** :

1. Telecharger l'archive binaire (Binary zip archive) :

   <https://archive.apache.org/dist/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.zip>

2. Extraire l'archive dans `D:\Dev\Outils\Maven` (par exemple), ce qui donne :

   ```
   D:\Dev\Outils\Maven\apache-maven-3.9.11
   ```

3. Creer une variable d'environnement systeme ou utilisateur :

   ```
   MAVEN_HOME = D:\Dev\Outils\Maven\apache-maven-3.9.11
   ```

4. Ajouter `%MAVEN_HOME%\bin` a la variable `Path`.

5. Ouvrir un **nouveau** terminal et verifier :

   ```powershell
   mvn -v
   ```

   Sortie attendue :

   ```
   Apache Maven 3.9.11
   Maven home: D:\Dev\Outils\Maven\apache-maven-3.9.11
   Java version: 21...
   ```

   > **Important** : apres avoir modifie `JAVA_HOME`, `MAVEN_HOME` ou `Path`,
   > fermer **tous** les terminaux puis en ouvrir un nouveau. Si le terminal est
   > integre a VS Code, redemarrer VS Code completement (un simple nouvel onglet
   > conserve l'ancien environnement).

6. Le depot local Maven est `C:\Users\<utilisateur>\.m2\repository` (celui
   presente dans l'atelier). Pour changer son emplacement (optionnel), copier
   `conf\settings.xml` dans `.m2` et renseigner `<localRepository>`.

> Sur ce poste, Maven 3.9.11 est installe dans
> `D:\Dev\Outils\Maven\apache-maven-3.9.11`, avec
> `MAVEN_HOME`, `JAVA_HOME` et `Path` configures au niveau utilisateur.

---

## 5. Execution de l'application

Les commandes ci-dessous partent de la racine du projet :

```powershell
cd "D:\Mini Project\stage-management"
```

### 5.1 Option A - MySQL via XAMPP (recommandee)

1. Demarrer MySQL (voir 4.2) et verifier la connexion.
2. Configurer `JAVA_HOME` (voir 4.1).
3. Lancer :

   ```powershell
   mvn spring-boot:run
   ```

4. Attendre la ligne :

   ```
   Started StageManagementApplication in ... seconds
   ```

5. Ouvrir <http://localhost:8080>.
6. Au premier demarrage (base vide), `DataInitializer` insere le jeu de donnees
   de demonstration. Les demarrages suivants conservent les donnees existantes.
7. Arreter avec `Ctrl+C` dans le terminal.

Configuration de la base (profil par defaut) :

| Parametre | Valeur |
|---|---|
| Hote / port | `localhost:3306` |
| Base | `springJpaStages` (creee automatiquement) |
| Utilisateur | `root` |
| Mot de passe | (vide) |
| Schema | cree/mis a jour par Hibernate (`ddl-auto=update`) |
| Fichier | `src/main/resources/application.properties` |

Les donnees etant persistantes, elles sont conservees apres un redemarrage.
Pour repartir d'une base vide et reinitialiser les donnees de demonstration :

```powershell
D:\xampp\mysql\bin\mysql.exe -u root -e "DROP DATABASE IF EXISTS springJpaStages;"
mvn spring-boot:run
```

### 5.2 Option B - Jar executable

1. Demarrer MySQL (voir 4.2) : les tests s'executent pendant le packaging.
2. Construire le jar :

   ```powershell
   mvn clean package
   ```

   Pour ignorer les tests (sans MySQL) : `mvn clean package -DskipTests`

3. Executer :

   ```powershell
   java -jar target\stage-management-0.0.1-SNAPSHOT.jar
   ```

4. Variante pour changer le port :

   ```powershell
   java -jar target\stage-management-0.0.1-SNAPSHOT.jar --server.port=9090
   ```

### 5.3 Option C - Docker Compose

Prerequis : Docker Desktop demarre. Docker embarque son propre MySQL 8.4 :
XAMPP n'est pas utilise dans ce mode.

```powershell
# Construction des images et demarrage (MySQL + application)
docker compose up --build

# En arriere-plan
docker compose up --build -d

# Suivre les logs de l'application
docker compose logs -f app

# Arreter et supprimer les conteneurs
docker compose down

# Arreter et supprimer aussi les donnees MySQL
docker compose down -v
```

| Element | Valeur |
|---|---|
| Application | <http://localhost:8080> |
| MySQL (hote) | `localhost:3307` (mappe sur 3306 dans le conteneur) |
| Base | `stages` |
| Utilisateur applicatif | `stages` / `stages` |
| Root MySQL | `root` / `root` |
| Profil Spring actif | `docker` |

Le service `app` attend que MySQL soit sain (healthcheck) avant de demarrer.
Apres une modification du code, relancer `docker compose up --build`.

### 5.4 Aide-memoire des commandes

| But | Commande |
|---|---|
| Demarrer MySQL (XAMPP) | `D:\xampp\mysql_start.bat` |
| Lancer l'application | `mvn spring-boot:run` |
| Lancer sur un autre port | `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090` |
| Compiler | `mvn compile` |
| Tests (MySQL demarre) | `mvn test` |
| Packager (tests inclus) | `mvn clean package` |
| Packager sans tests | `mvn clean package -DskipTests` |
| Nettoyer | `mvn clean` |
| Jar executable | `java -jar target\stage-management-0.0.1-SNAPSHOT.jar` |
| Docker | `docker compose up --build` |

### 5.5 Diagnostic des problemes courants

| Symptome | Cause probable | Solution |
|---|---|---|
| `java n'est pas reconnu` | `JAVA_HOME`/`Path` non configures | Refaire 4.1 puis rouvrir le terminal |
| `invalid target release: 21` / `class file version` | JDK < 21 utilise | Verifier `java -version` et `JAVA_HOME` |
| `Communications link failure` / `Connection refused` | MySQL (XAMPP) arrete | Demarrer MySQL (4.2) puis relancer |
| `Access denied for user 'root'` | Mot de passe MySQL defini | Renseigner `spring.datasource.password` |
| `Unknown database 'springjpastages'` | Creation automatique impossible | Creer la base dans phpMyAdmin ou avec `CREATE DATABASE springJpaStages;` |
| Tests en echec avec `Connection refused` | MySQL non demarre pendant `mvn test` | Demarrer XAMPP MySQL puis relancer les tests |
| `Port 8080 was already in use` | Un autre processus ecoute | Identifier le processus avec `netstat -ano` (filtrer sur `:8080`) puis `taskkill /PID <pid> /F`, ou changer de port |
| Port 3306 deja utilise | Un autre MySQL est demarre | Arreter l'autre service ou changer le port XAMPP + la configuration |
| Donnees non reinitialisees | MySQL est persistant | Supprimer la base `springJpaStages` puis redemarrer |
| Erreur 500 sur une vue Thymeleaf | Collection paresseuse / template | Verifier les logs ; `spring.jpa.open-in-view=true` |
| `mvn n'est pas reconnu` | `MAVEN_HOME`/`Path` non configures | Installer Maven (4.3) puis rouvrir le terminal |
| `mvn` introuvable malgre l'installation | Terminal ouvert avant la configuration (ancien environnement) | Fermer tous les terminaux (et VS Code) puis rouvrir ; verifier `where.exe mvn` |
| `JAVA_HOME` non defini (au lancement de Maven) | Variable absente de la session courante | Rouvrir le terminal, ou definir `$env:JAVA_HOME` dans la session |
| PowerShell refuse la commande | Politique d'execution | Utiliser `cmd /c mvn ...` |
| Premier build tres long | Telechargement Maven/dependances | Attendre la fin (connexion Internet requise) |

---

## 6. URLs et donnees de demonstration

| URL | Description |
|---|---|
| `/` | Accueil : choix de l'espace (administrateur / enseignant / etudiant) |
| `/admin` | Espace administrateur : tableau de bord et statistiques |
| `/etudiants` | Admin : liste / recherche / CRUD des etudiants |
| `/etudiants/{id}` | Espace etudiant : stage, avancement, reunions, livrables, observations |
| `/enseignants` | Admin : liste / CRUD des enseignants |
| `/enseignants/{id}` | Admin : detail d'un enseignant et de ses etudiants |
| `/enseignant` | Espace enseignant : choix du profil |
| `/enseignant/{id}` | Espace enseignant : etudiants affectes, etapes a valider, suivi |
| `/etudiant` | Espace etudiant : choix du profil |
| `/entreprises` | Admin : liste / CRUD des entreprises |
| `/stages` | Admin : liste filtrable des stages |
| `/stages/{id}` | Suivi detaille : reunions, observations, etapes, livrables |
| `/csr/index.html` | Interface CSR (JavaScript + API REST) |
| `/api/...` | API REST (voir section 7) |

Donnees creees au premier demarrage (si la base est vide) :

- 3 entreprises : Vermeg, Sagemcom, Telnet
- 3 enseignants : M. Zayani, Mme Trabelsi, M. Ben Amor
- 5 etudiants (dont 1 sans encadrant et 2 sans stage)
- 3 stages (en cours, en attente, valide) avec reunions, observations,
  etapes, livrables et declarations d'avancement

Ces donnees sont stockees dans MySQL (`springJpaStages`) et persistent entre
les redemarrages. Pour les consulter : phpMyAdmin (<http://localhost/phpmyadmin>)
ou `D:\xampp\mysql\bin\mysql.exe -u root`.

---

## 7. API REST

### 7.1 Endpoints

| Methode | Endpoint | Description |
|---|---|---|
| GET/POST | `/api/etudiants` | Lister (`?q=`) / creer |
| GET/PUT/DELETE | `/api/etudiants/{id}` | Consulter / modifier / supprimer |
| GET | `/api/etudiants/encadrant/{enseignantId}` | Etudiants d'un encadrant |
| PUT | `/api/etudiants/{id}/encadrant/{enseignantId}` | Affecter un encadrant |
| GET/POST | `/api/enseignants` | Lister (`?q=`) / creer |
| GET/PUT/DELETE | `/api/enseignants/{id}` | Consulter / modifier / supprimer |
| GET/POST | `/api/entreprises` | Lister (`?q=`) / creer |
| GET/PUT/DELETE | `/api/entreprises/{id}` | Consulter / modifier / supprimer |
| GET/POST | `/api/stages` | Lister (`?q=` ou `?statut=`) / creer |
| GET/PUT/DELETE | `/api/stages/{id}` | Consulter / modifier / supprimer |
| GET/POST | `/api/reunions?stageId=` | Lister / creer une reunion |
| PUT/DELETE | `/api/reunions/{id}` | Modifier / supprimer |
| GET/POST | `/api/observations?stageId=` | Lister / creer |
| DELETE | `/api/observations/{id}` | Supprimer |
| GET/POST | `/api/etapes?stageId=` | Lister / creer |
| PUT | `/api/etapes/{id}/valider?enseignantId=` | Valider une etape |
| DELETE | `/api/etapes/{id}` | Supprimer |
| GET/POST | `/api/livrables?stageId=` | Lister / deposer |
| DELETE | `/api/livrables/{id}` | Supprimer |
| GET/POST | `/api/avancements?stageId=` | Lister / declarer un avancement |
| DELETE | `/api/avancements/{id}` | Supprimer |
| GET | `/api/statistiques` | Indicateurs agreges |

### 7.2 Exemples (PowerShell)

```powershell
# Lister les etudiants
Invoke-RestMethod http://localhost:8080/api/etudiants

# Creer un etudiant
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/etudiants `
  -ContentType 'application/json' `
  -Body '{"nom":"Ben Salah","prenom":"Ahmed","email":"ahmed.bensalah@iit.tn","numeroEtudiant":"ETU-2026-001","filiere":"Genie Logiciel","niveau":"3eme annee"}'

# Declarer un avancement pour le stage 1
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/avancements `
  -ContentType 'application/json' `
  -Body '{"dateDeclaration":"2026-11-22","pourcentage":60,"commentaire":"Tests en cours","stageId":1}'

# Valider l'etape 2 au nom de l'enseignant 1
Invoke-RestMethod -Method Put -Uri 'http://localhost:8080/api/etapes/2/valider?enseignantId=1'

# Statistiques
Invoke-RestMethod http://localhost:8080/api/statistiques
```

### 7.3 Exemples (curl, bash)

```bash
curl -s http://localhost:8080/api/etudiants

curl -s -X POST http://localhost:8080/api/etudiants \
  -H 'Content-Type: application/json' \
  -d '{"nom":"Ben Salah","prenom":"Ahmed","email":"ahmed.bensalah@iit.tn"}'

curl -s 'http://localhost:8080/api/stages?statut=EN_COURS'
```

### 7.4 Format d'erreur (RFC 7807)

```json
{
  "type": "about:blank",
  "title": "Validation echouee",
  "status": 400,
  "detail": "Les donnees envoyees sont invalides.",
  "instance": "/api/etudiants",
  "erreurs": {
    "email": "L'email est obligatoire"
  }
}
```

| Code | Signification |
|---|---|
| 400 | Validation, parametre manquant ou invalide |
| 404 | Ressource inexistante |
| 409 | Regle metier non respectee (email deja utilise, etudiant deja en stage, suppression bloquee...) |

---

## 8. Tests

**Prerequis : MySQL demarre (XAMPP).** Les tests ne reposent sur aucune base
embarquee ; ils utilisent deux bases MySQL dediees, creees automatiquement :

- `springJpaStagesTest` : tests `@SpringBootTest` / MVC ;
- `springJpaStagesJpaTest` : tests `@DataJpaTest` (isolee).

Le schema est cree/mis a jour automatiquement par Hibernate ; la base de
developpement `springJpaStages` n'est pas touchee.

```powershell
mvn test
```

Rapports : `target/surefire-reports/`.

| Classe de test | Type | Ce qui est verifie |
|---|---|---|
| `StageManagementApplicationTests` | Integration | Chargement du contexte Spring |
| `StageRepositoryTest` | `@DataJpaTest` | Requetes derivees et agregation JPQL |
| `EtudiantServiceTest` | Unitaire (Mockito) | Affectation d'encadrant, blocage de suppression |
| `DeclarationAvancementServiceTest` | Unitaire (Mockito) | Date par defaut, pourcentage 0-100 |
| `StageRestControllerTest` | `@WebMvcTest` | Liste JSON, 404 ressource, 400 parametre |
| `StageMvcControllerTest` | Integration MVC | Non-regression : formulaire de reunion |
| `EtudiantMvcControllerTest` | Integration MVC | Declaration d'avancement et depot de livrable |
| `SpaceControllerTest` | Integration MVC | Accueil et acces aux trois espaces acteurs |

Resultat attendu : **13 tests, 0 echec**.

> `clean package` execute les tests : MySQL doit etre demarre. Pour construire
> le jar sans base, utiliser `-DskipTests`.

---

## 9. Couverture du cahier des charges

| Exigence du sujet | Mise en oeuvre |
|---|---|
| Gerer les etudiants et les enseignants | CRUD SSR `/etudiants`, `/enseignants` + API REST |
| Gerer les entreprises d'accueil | CRUD SSR `/entreprises` + API REST |
| Affecter un enseignant a un etudiant | Selecteur dans la fiche etudiant + `PUT /api/etudiants/{id}/encadrant/{enseignantId}` |
| Consulter les stages et leurs statistiques | `/stages`, tableau de bord `/`, `GET /api/statistiques` |
| Consulter les etudiants affectes (enseignant) | `/enseignants/{id}` |
| Gerer les reunions de suivi | Ajout/suppression en SSR, CRUD complet en REST |
| Ajouter observations et recommandations | Formulaire `/stages/{id}` (type Observation/Recommandation) + API |
| Valider les etapes de progression | Bouton "Valider" dans `/stages/{id}` + `PUT /api/etapes/{id}/valider` |
| Consulter les informations de son stage (etudiant) | `/etudiants/{id}` : entreprise, periode, statut, reunions, etapes |
| Declarer l'avancement de ses travaux (etudiant) | Formulaire `/etudiants/{id}` + `POST /api/avancements` |
| Deposer informations/liens des livrables (etudiant) | Formulaire `/etudiants/{id}` + `POST /api/livrables` |
| Consulter les observations de son encadrant | Section dediee dans `/etudiants/{id}` |
| Trois espaces acteurs | Accueil `/` + `/admin`, `/enseignant`, `/etudiant` |
| SSR (Thymeleaf, MVC, formulaires, navigation) | `controller/web` + `templates/` (layout + 17 vues) |
| REST + CSR (DTO, appels HTTP) | `controller/rest` + `dto` + `static/csr/index.html` |
| Couches et paquetages coherents | `model`, `repository`, `service`, `dto`, `controller`, `exception`, `config` |
| Docker + Docker Compose | `Dockerfile` multi-stage + `docker-compose.yml` |
| Tests de persistance et de service | 13 tests JUnit/Mockito/MockMvc/DataJpa sur MySQL |
| GitHub, branches, Pull Requests | Workflow propose (section 11) - depot a initialiser |
| Rapport UML + video | Etape suivante |

---

## 10. Organisation de l'equipe

| Etudiant | Perimetre | Paquetages principaux |
|---|---|---|
| 1 - SSR | Vues Thymeleaf, formulaires, navigation | `templates/`, `controller/web` |
| 2 - REST/CSR | API REST, DTO, interface CSR | `controller/rest`, `dto`, `static/csr` |
| 3 - Metier/persistance | Entites, services, repositories, Docker, tests | `model`, `repository`, `service`, `config`, `Dockerfile` |

---

## 11. Workflow Git/GitHub

1. Creer le depot GitHub et inviter les 3 membres.
2. Branches permanentes : `main` (rendu) et `develop` (integration).
3. Une branche par fonctionnalite, nommee par domaine :
   `feature/ssr-etudiants`, `feature/rest-stages`, `feature/jpa-relations`,
   `feature/docker-ci`.
4. Chaque membre pousse sa branche et ouvre une **Pull Request** vers `develop`
   (description, captures, tests executes).
5. Revue obligatoire par un autre membre avant fusion ; pas de push direct sur
   `main`/`develop`.
6. En cas de conflit : rebase de la branche sur `develop`, resolution locale,
   `mvn test`, puis mise a jour de la PR.
7. Taguer la version finale (`v1.0`) sur `main` pour le rendu.

Cette organisation fournit les elements demandes dans la section 5 du rapport
(branches, PRs, repartition, resolution des conflits).

---

## 12. Limites et perspectives

- **Authentification / roles** : hors perimetre du sujet ; les trois acteurs sont
  separes par des ecrans dedies. Spring Security pourrait etre ajoute.
- **Tests dependants de MySQL** : les tests d'integration necessitent XAMPP
  demarre (pas de base embarquee) ; une alternative serait Testcontainers.
- **Pagination** : les listes principales ne sont pas paginees (l'atelier
  Thymeleaf montre `Page`/`Pageable` ; extension simple sur les listes de stages
  et d'etudiants).
- **`open-in-view`** : active pour simplifier l'affichage des collections
  paresseuses ; une evolution consisterait a utiliser des DTO de lecture ou des
  fetch joins.
- **CI/CD** : un pipeline GitHub Actions (`mvn verify` + build Docker) peut etre
  ajoute pour automatiser les tests (avec un service MySQL).
- **Docker** : les fichiers sont fournis ; la validation d'execution doit etre
  faite sur un poste disposant de Docker Desktop.
- **Rapport et video** : a produire (diagrammes de paquetages, classes par couche,
  deploiement, sequences SSR et CSR).

---

## 13. Journal de validation

Validation realisee le **2026-11-22** sur le poste de developpement, apres
suppression de H2 et du wrapper Maven, avec la base XAMPP/MySQL.

### 13.1 Environnement

| Element | Valeur |
|---|---|
| Systeme | Windows 11 (amd64) |
| JDK | Eclipse Temurin 21.0.13.1 (`JAVA_HOME=C:\Users\ahmedr2\tools\jdk21`) |
| Maven | Apache Maven 3.9.11 (`D:\Dev\Outils\Maven\apache-maven-3.9.11`) |
| Base de donnees | XAMPP MariaDB 10.4.32, `localhost:3306`, utilisateur `root` sans mot de passe |
| Base applicative | `springJpaStages` |
| Bases de test | `springJpaStagesTest`, `springJpaStagesJpaTest` |
| Framework | Spring Boot 3.5.5 / Java 21 |
| Driver JDBC | MySQL Connector/J 9.4.0 |

Controles d'outillage :

| Controle | Resultat |
|---|---|
| `java -version` | OpenJDK 21.0.13.1 LTS |
| `mvn -v` | Apache Maven 3.9.11 + Java 21 |
| H2 dans les dependances | Absent (`dependency:tree`) |
| Wrapper Maven (`mvnw`, `.mvn/`) | Absent |

### 13.2 Build et tests automatises

| Commande | Resultat |
|---|---|
| `mvn clean package` | **BUILD SUCCESS** |
| `mvn test` | **13 tests, 0 echec, 0 erreur, 0 ignore** |

Detail par classe :

| Classe de test | Tests | Resultat |
|---|---|---|
| `StageManagementApplicationTests` | 1 | OK |
| `StageRepositoryTest` | 1 | OK |
| `EtudiantServiceTest` | 2 | OK |
| `DeclarationAvancementServiceTest` | 2 | OK |
| `StageRestControllerTest` | 3 | OK |
| `EtudiantMvcControllerTest` | 2 | OK |
| `StageMvcControllerTest` | 1 | OK |
| `SpaceControllerTest` | 1 | OK |

### 13.3 Validation fonctionnelle (SSR)

Application demarree sur le port de validation `8099`, puis verification HTTP de
chaque route :

| Route | Contenu verifie | Code |
|---|---|---|
| `/` | Accueil : 3 cartes Administration / Enseignant / Etudiant | 200 |
| `/admin` | Badge administrateur, statistiques, liens CRUD | 200 |
| `/enseignant` | Liste de choix des enseignants | 200 |
| `/enseignant/1` | Badge enseignant, etudiants affectes, etapes a valider | 200 |
| `/enseignant/2` | Etudiant sans stage affiche "Aucun stage" | 200 |
| `/etudiant` | Liste de choix des etudiants | 200 |
| `/etudiants/1` | Badge etudiant, avancement, livrable, observations | 200 |
| `/etudiants/5` | Etudiant sans stage : message explicite | 200 |
| `/etudiants`, `/enseignants` | Listes CRUD | 200 |
| `/enseignants/1`, `/stages`, `/stages/1` | Detail, liste et fiche stage | 200 |
| `/entreprises` | Liste des entreprises | 200 |
| `/csr/index.html` | Page CSR chargee | 200 |
| `/api/statistiques` | JSON des indicateurs | 200 |
| `/h2-console` | H2 supprime | 404 |

Controles de contenu et de logs :

- Navbar : `Accueil`, `Administration`, `Espace enseignant`, `Espace etudiant`,
  `Interface CSR` presents sur toutes les pages.
- Fiche stage `/stages/1` : reunions, observations, etapes et livrables affiches.
- Aucun `ERROR` dans les logs ; sortie d'erreur vide.

### 13.4 Contrats de l'API REST

| Scenario | Attendu | Obtenu |
|---|---|---|
| `GET /api/etudiants`, `/api/stages`, `/api/statistiques` | 200 | 200 |
| `GET /api/etudiants/999` | 404 (ProblemDetail) | 404 |
| `POST /api/etudiants` valide | 201 | 201 |
| `POST /api/etudiants` email duplique | 409 | 409 |
| `POST /api/etudiants` payload invalide | 400 | 400 |
| `DELETE /api/entreprises/1` (entreprise avec stages) | 409 | 409 |
| `POST /api/avancements` etudiant deja en stage | 409 | 409 |
| `POST /api/avancements` pourcentage > 100 | 400 | 400 |
| `PUT /api/reunions/{id}`, `DELETE /api/reunions/{id}` | 200 / 204 | 200 / 204 |
| Parametre obligatoire manquant | 400 | 400 |

### 13.5 Persistance MySQL

| Controle | Resultat |
|---|---|
| Tables creees par Hibernate | `etudiant`, `enseignant`, `entreprise`, `stage`, `reunion`, `observation`, `etape_progression`, `livrable`, `declaration_avancement` |
| Donnees de demonstration | 3 entreprises, 3 enseignants, 5 etudiants, 3 stages, 2 reunions, 3 observations, 8 etapes, 3 livrables, 3 declarations |
| Creation via `POST /api/etudiants` puis redemarrage | Etudiant toujours present (`id=6`) |
| Nombre de stages apres redemarrage | 3 (inchange) : `DataInitializer` ne duplique pas |
| Nettoyage | Etudiant de test supprime via `DELETE` |

### 13.6 Points non valides a ce stade

| Element | Statut |
|---|---|
| `docker compose up --build` | Non execute : Docker Desktop non installe sur le poste |
| Depot GitHub (branches, PRs) | A initialiser par l'equipe |
| Rapport UML (15-20 pages) | A produire |
| Video de demonstration | A produire |
