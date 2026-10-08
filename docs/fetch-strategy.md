# Strategie de chargement et cycle de vie des associations

Ce document decrit les associations JPA du modele AutoLoc, leur cote
proprietaire, leur strategie de chargement et les choix de cascade.

## Regles generales

- Les associations `@ManyToOne` sont forcees a `FetchType.LAZY` pour eviter
  de charger automatiquement les entites parentes.
- Les collections `@OneToMany` et `@ManyToMany` restent en `LAZY`.
- Les relations bidirectionnelles utilisent `mappedBy` sur le cote inverse.
- Aucune entite n'utilise `@Data` : les getters et setters Lombok sont
  declares explicitement afin d'eviter les boucles dans `toString` et
  `equals/hashCode`.
- Les cascades ne sont utilisees que pour une composition. Les associations
  partagees ne propagent pas la suppression de leur parent.

## Associations

| Association | Cote proprietaire et cle etrangere | Cote inverse | Fetch | Cascade / orphanRemoval |
|---|---|---|---|---|
| `Agence` - `Employe` | `Employe.agence`, colonne `id_agence` dans `employe` | `Agence.employes`, `mappedBy = "agence"` | `LAZY` des deux cotes | Aucune cascade : un employe est gere independamment |
| `Agence` - `Vehicule` | `Vehicule.agence`, colonne `id_agence` dans `vehicule` | `Agence.vehicules`, `mappedBy = "agence"` | `LAZY` des deux cotes | Aucune cascade : un vehicule survit a l'agence |
| `Agence` - `Reservation` | `Reservation.agence`, colonne `id_agence` dans `reservation` | `Agence.reservations`, `mappedBy = "agence"` | `LAZY` des deux cotes | Aucune cascade |
| `Client` - `Reservation` | `Reservation.client`, colonne `id_client` dans `reservation` | `Client.reservations`, `mappedBy = "client"` | `LAZY` des deux cotes | Aucune cascade : les reservations ont leur propre cycle de vie |
| `Vehicule` - `Reservation` | `Reservation.vehicule`, colonne `id_vehicule` dans `reservation` | `Vehicule.reservations`, `mappedBy = "vehicule"` | `LAZY` des deux cotes | Aucune cascade |
| `Vehicule` - `Maintenance` | `Maintenance.vehicule`, colonne `id_vehicule` dans `maintenance` | `Vehicule.maintenances`, `mappedBy = "vehicule"` | `LAZY` des deux cotes | Aucune cascade |
| `Reservation` - `Contrat` | `Contrat.reservation`, colonne `id_reservation` dans `contrat`, unique et non nulle | `Reservation.contrat`, `mappedBy = "reservation"` | Proprietaire `LAZY`; inverse JPA par defaut | Aucune cascade |
| `Contrat` - `Paiement` | `Paiement.contrat`, colonne generee `contrat_id_contrat` dans `paiement` | `Contrat.paiements`, `mappedBy = "contrat"` | `LAZY` | `CascadeType.ALL` et `orphanRemoval = true` |
| `Vehicule` - `Equipement` | `Vehicule.equipements`, table `vehicule_equipement` avec `id_vehicule` et `id_equipement` | `Equipement.vehicules`, `mappedBy = "equipements"` | `LAZY` des deux cotes | Aucune cascade : les equipements sont reutilisables |

## Cas Contrat - Paiement

Un paiement ne peut pas exister sans contrat. Le contrat est donc le parent
fonctionnel et la collection `Contrat.paiements` utilise :

```java
@OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL,
        orphanRemoval = true, fetch = FetchType.LAZY)
private List<Paiement> paiements = new ArrayList<>();
```

`CascadeType.ALL` propage la persistance, la mise a jour et la suppression du
contrat vers ses paiements. `orphanRemoval = true` supprime un paiement retire
de la collection. Le cote `Paiement.contrat` est le cote proprietaire et porte
la cle etrangere.

## Limite des relations OneToOne lazy

Le cote inverse `Reservation.contrat` ne force pas `FetchType.LAZY`. JPA ne
garantit pas le lazy loading sur le cote non proprietaire d'une association
`@OneToOne`; Hibernate peut donc charger ce cote comme une relation eager.
Pour les lectures controlees, utiliser une requete dediee dans une transaction.
