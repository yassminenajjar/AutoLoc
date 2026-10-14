# Atelier 4 — IoC, DI et couche Service

## A. Modes d'injection comparés

| Mode | Caractéristiques | Choix AutoLoc |
|---|---|---|
| Constructeur | La dépendance est visible, obligatoire et peut être stockée dans un champ `final`. La classe reste testable hors conteneur. | Retenu avec `@RequiredArgsConstructor` : Spring injecte l'unique constructeur. |
| Attribut | La dépendance est cachée, peut rester non initialisée hors Spring et est signalée par SonarQube comme moins testable. | Non utilisé. |

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---|---|---|---|
| `AgenceServiceImpl` | `IAgenceRepository` | Constructeur | Dépend d'une abstraction et reste sans état. |
| `ClientServiceImpl` | `IClientRepository` | Constructeur | Même contrat pour le CRUD du client. |
| `ContratServiceImpl` | `IContratRepository` | Constructeur | Le contrat porte la composition des paiements. |
| `EmployeServiceImpl` | `IEmployeRepository` | Constructeur | La persistance est déléguée au repository Spring Data. |
| `EquipementServiceImpl` | `IEquipementRepository` | Constructeur | Les équipements sont réutilisables et gérés indépendamment. |
| `MaintenanceServiceImpl` | `IMaintenanceRepository` | Constructeur | Contrôle la cohérence des dates avant persistance. |
| `PaiementServiceImpl` | `IPaiementRepository` | Constructeur | Lecture seule : les écritures passent par le contrat. |
| `ReservationServiceImpl` | `IReservationRepository` | Constructeur | Contrôle l'ordre des dates de réservation. |
| `VehiculeServiceImpl` | `IVehiculeRepository` | Constructeur | Contrôle le tarif journalier et délègue le CRUD. |

## C. Messages d'erreur du conteneur

1. `required a bean of type ... IContratService` signifie généralement que
   l'implémentation n'est pas annotée `@Service` ou qu'elle est hors du scan
   de `tn.esprit.autoloc`. Il faut ajouter `@Service` et placer la classe dans
   un sous-package de la classe principale.
2. `Consider defining a bean of type ... IContratRepository` indique que le
   repository n'est pas détecté ou que sa dépendance n'est pas correctement
   déclarée. Il faut conserver une interface `JpaRepository` dans le package
   `repository` scanné par Spring Data.
3. Une erreur d'ambiguïté entre plusieurs implémentations d'une interface
   signifie que Spring trouve plusieurs beans candidats. Il faut choisir avec
   `@Primary` ou qualifier explicitement le constructeur avec `@Qualifier`.

## D. Anomalies SonarQube for IDE

| Règle / anomalie | Explication | Correction |
|---|---|---|
| Injection par attribut | Rend la dépendance implicite et les tests plus difficiles. | Champs `final` et injection par constructeur. |
| Exception générique ou absence de garde | Une erreur technique peu précise ne décrit pas l'absence d'une ressource métier. | `ResourceNotFoundException` dans `findById` et `deleteById`. |
| Méthode `update` qui sauvegarde directement l'objet reçu | Peut modifier l'identifiant ou écraser une collection associée. | Charger l'existant puis recopier seulement les attributs modifiables. |
| Service avec état mutable | Un service singleton est partagé entre les requêtes et provoquerait des interférences. | Les implémentations ne conservent que des dépendances `final`. |

## E. Questions de compréhension

1. Il n'y a pas de `new` écrit dans le service. Spring crée
   `ContratServiceImpl` lors de l'instanciation du contexte et lui injecte le
   proxy `IContratRepository` via son constructeur.
2. Un contrôleur dépend de `IContratService` pour respecter le principe
   d'inversion de dépendance, faciliter les tests et permettre de remplacer
   l'implémentation sans modifier le contrôleur.
3. Le singleton est partagé par tous les threads. Un attribut mutable comme
   `contratCourant` pourrait être remplacé par une autre requête avant d'être
   utilisé, et mélanger deux contrats.
4. Le chargement de l'entité existante protège l'identifiant et les champs
   non modifiables, notamment la collection de paiements avec
   `orphanRemoval`. On ne remplace donc pas l'état complet par un objet
   partiellement rempli.
5. `@Component` permet à Spring de détecter une classe ; `@Bean` déclare
   explicitement l'objet retourné par une méthode de configuration.
   `@Primary` désigne un candidat par défaut, tandis que `@Qualifier` choisit
   précisément un bean. `IPaiementService` est en lecture seule car le contrat
   est le parent métier responsable de créer, modifier et supprimer ses
   paiements, avec la cascade JPA.
