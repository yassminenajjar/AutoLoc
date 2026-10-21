# Atelier 5 - API REST, Postman et SpringDoc

## A. Endpoints

Tous les contrôleurs sont accessibles sous le contexte `/api` et dépendent
uniquement de leur interface de service.

| Contrôleur | Verbe et route | Service appelé | Succès | Erreurs |
| --- | --- | --- | --- | --- |
| AgenceController | GET/POST/PUT/DELETE `/agences[/{id}]` | IAgenceService | 200/201/204 | 400/404 |
| ClientController | GET/POST/PUT/DELETE `/clients[/{id}]` | IClientService | 200/201/204 | 400/404 |
| ContratController | GET/POST/PUT/DELETE `/contrats[/{id}]` | IContratService | 200/201/204 | 400/404 |
| EmployeController | GET/POST/PUT/DELETE `/employes[/{id}]` | IEmployeService | 200/201/204 | 400/404 |
| EquipementController | GET/POST/PUT/DELETE `/equipements[/{id}]` | IEquipementService | 200/201/204 | 400/404 |
| MaintenanceController | GET/POST/PUT/DELETE `/maintenances[/{id}]` | IMaintenanceService | 200/201/204 | 400/404 |
| PaiementController | GET `/paiements[/{id}]` | IPaiementService | 200 | 404, 405 |
| ReservationController | GET/POST/PUT/DELETE `/reservations[/{id}]` | IReservationService | 200/201/204 | 400/404 |
| VehiculeController | GET/POST/PUT/DELETE `/vehicules[/{id}]` | IVehiculeService | 200/201/204 | 400/404 |

La documentation interactive est disponible sur
`http://localhost:8080/api/swagger-ui.html` et le document OpenAPI sur
`http://localhost:8080/api/v3/api-docs`.

## B. Annotations Jackson

`Contrat.paiements` est annoté avec `@JsonManagedReference("contrat-paiements")`
et `Paiement.contrat` avec l'annotation inverse correspondante. Le contrat peut
ainsi exposer ses paiements sans sérialiser à nouveau le contrat depuis chaque
paiement. Les collections inverses des autres associations sont ignorées avec
`@JsonIgnore` afin d'éviter les cycles JSON.

`@JsonBackReference` ignore la propriété inverse uniquement lors de la
sérialisation et conserve la possibilité de la désérialiser dans une composition.
`@JsonIgnore` masque complètement la propriété pour la lecture et l'écriture JSON.

## C. Postman et Swagger

Postman sert à créer une collection de requêtes rejouables et à vérifier les
codes HTTP avec des scripts. Swagger UI décrit le contrat OpenAPI généré et
permet d'essayer directement chaque endpoint. Par exemple, Postman vérifie
qu'un `GET /api/contrats/9999` renvoie 404, tandis que Swagger affiche ce
paramètre et sa réponse documentée.

## D. Anomalies traitées

| Règle ou problème | Correction |
| --- | --- |
| Injection par attribut | Les contrôleurs utilisent un champ `final` et `@RequiredArgsConstructor`. |
| Accès aux repositories depuis le web | Les contrôleurs dépendent des interfaces `I...Service`. |
| Boucles de sérialisation JPA | Associations inverses annotées avec Jackson. |
| Endpoints non documentés | SpringDoc et `OpenApiConfig` fournissent la documentation OpenAPI. |

## E. Questions de compréhension

1. `DispatcherServlet` reçoit la requête, consulte le mapping construit à partir
   des annotations MVC, puis appelle la méthode du contrôleur correspondante.
2. L'interface de service respecte la séparation des couches et permet de
   remplacer l'implémentation sans coupler le web à JPA.
3. Un POST crée une ressource et renvoie 201 ; DELETE réussit sans corps et
   renvoie 204. Une ressource inexistante est une erreur client 404, pas une
   panne serveur 500.
4. Paiement est en lecture seule car il est créé par cascade depuis Contrat.
   Un POST `/api/paiements` renvoie donc 405 Method Not Allowed.
5. Exposer les entités JPA révèle des détails internes, couple le JSON au schéma
   et peut créer des cycles. La composition Contrat-Paiement utilise les
   références Jackson, tandis que les collections inverses d'Agence sont
   ignorées car elles ne font pas partie du payload de création.
