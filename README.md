# GymFlow

GymFlow est une application Java de gestion de salle de sport.  
Elle permet de gérer les comptes, les membres, les abonnements, les coachs, les disponibilités, les rendez-vous, les parrainages et les recherches avancées.

Le projet utilise une architecture en couches :

- `modelPackage` : classes métier
- `dataAccessPackage` : accès à la base de données
- `businessPackage` : logique métier
- `controllerPackage` : lien entre interface et logique métier
- `viewPackage` : interface JavaFX
- `exceptionPackage` : exceptions personnalisées
- `securityPackage` : gestion sécurisée des mots de passe

---

## Fonctionnalités principales

### Comptes et connexion

- Création d’un compte utilisateur
- Connexion avec nom d’utilisateur et mot de passe
- Mots de passe stockés sous forme hashée avec PBKDF2
- Gestion des rôles :
  - Administrateur
  - Membre non inscrit
  - Membre inscrit
  - Coach

---

### Gestion des membres

L’administrateur peut :

- Lister les membres
- Ajouter un membre
- Modifier un membre
- Supprimer un membre
- Supprimer automatiquement les dépendances liées :
  - rendez-vous
  - paiements
  - parrainages
  - abonnement
  - compte personne

Un utilisateur connecté peut aussi s’inscrire lui-même comme membre.

---

### Abonnements

L’application gère trois grades d’abonnement :

| Grade | Droits |
|---|---|
| Basic | Accès à la salle normale |
| Standard | Accès à la salle normale + réservation coach |
| Premium | Accès à la salle normale + réservation coach + installations |

Les prix sont calculés automatiquement selon le grade et la durée choisie.

---

### Rendez-vous

Un membre Standard ou Premium peut réserver un rendez-vous avec un coach.

La réservation vérifie :

- le membre possède un abonnement valide ;
- le grade d’abonnement autorise la réservation coach ;
- le créneau est disponible ;
- le coach est qualifié pour la spécialité choisie ;
- le membre n’a pas déjà un rendez-vous le même jour ;
- la salle sélectionnée existe.

Les membres peuvent voir leurs propres rendez-vous.  
Les coachs peuvent voir les rendez-vous liés à leurs disponibilités.  
Les administrateurs peuvent voir tous les rendez-vous.

---

### Coachs et disponibilités

Les coachs peuvent :

- consulter leurs disponibilités ;
- ajouter une disponibilité ;
- supprimer une disponibilité ;
- consulter leurs rendez-vous ;
- confirmer ou annuler un rendez-vous.

Chaque coach peut être lié à une ou plusieurs spécialités.

---

### Installations

Les membres Premium ont accès à un menu dédié aux installations.

Exemples :

- Sauna
- Piscine
- Espace détente
- Installations premium

---

### Recherches

L’application contient trois recherches avancées :

1. Rechercher les rendez-vous d’un membre entre deux dates
2. Rechercher les membres parrainés par un membre
3. Rechercher les créneaux disponibles des coachs qualifiés dans une spécialité entre deux dates

---

### Thread

Un bandeau d’information défilant est affiché dans l’application.  
Il fonctionne avec un thread dédié et ne fait aucun accès à la base de données.

---

## Technologies utilisées

- Java
- JavaFX
- MySQL
- JDBC
- IntelliJ IDEA
- MySQL Connector/J

---

## Prérequis

Avant de lancer le projet, il faut installer :

- JDK 26
- JavaFX SDK 26
- MySQL Server
- MySQL Workbench
- MySQL Connector/J

Versions utilisées pendant le développement :

```text
JDK : 26.0.1
JavaFX SDK : 26.0.1
MySQL Connector/J : 9.7.0
