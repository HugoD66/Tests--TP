# 🧪 Méthodologie des Tests - Ynov Bordeaux

Ce dépôt regroupe les différents travaux pratiques réalisés dans le cadre du cours **Méthodologie de tests et tests unitaires**, dispensé au sein du Mastère 2 à **Ynov Bordeaux**.

Objectifs pédagogiques du module :
- Apprendre à utiliser différents types de tests (unitaires, d'intégration, de mutation, etc.)
- Apprendre à écrire du code testable, maintenable et vérifiable
- Découvrir les outils de testing (Kotest, MockK, JaCoCo, PIT, etc.)
- Mettre en pratique le **Test Driven Development (TDD)** et le **Property-Based Testing**

---

## 🗂 Structure du projet

Ce dépôt contient plusieurs TP organisés par dossier, chacun illustrant un aspect particulier de la méthodologie de test :

### 📁 TP1 - Chiffrement de César

Objectif : Implémenter l’algorithme du **chiffrement de César** en TDD.

- Implémentation d’une fonction `cipher(char: String, key: Int)` décalant une lettre majuscule selon une clé donnée
- Prise en compte des cas limites : dépassement de `Z`, clés > 26, lettres non valides
- Écriture de **tests unitaires** simples avec **Kotest**
- Implémentation de **tests property-based** pour vérifier des invariants (réversibilité, stabilité, appartenance à l'alphabet, etc.)

> Exemples de propriétés testées :
> - `cipher(cipher(c, k), 26 - k) == c`
> - `cipher(c, 0) == c`
> - Résultat toujours une lettre majuscule

---

### 📁 TP2 - Gestion de livres (partie métier)

Objectif : Implémenter la **logique métier** d’un gestionnaire de bibliothèque.

- Création d’une entité `Book` (titre + auteur)
- Implémentation d’un **use case** pour ajouter et lister les livres
- Respect des règles métier :
    - Le titre et l’auteur ne doivent pas être vides
    - Les livres doivent être triés par ordre alphabétique
- Création d’un **port** pour abstraire la persistance des livres
- Utilisation de **MockK** pour tester les ports et comportements métier
- Ajout de **tests property-based** sur la cohérence de la liste

➡️ **CI/CD avec GitHub Actions** :
- Mise en place d’un pipeline de build et test automatisé via **GitHub Actions**
- À chaque commit/push sur le dépôt :
    - **Build de l’application**
    - **Exécution automatique de tous les tests**
    - Génération des **rapports de test** dans le pipeline


### 📁 TP3 - Intégration Web & Base de Données

Objectif : Exposer le domaine de gestion de livres via une API REST et persister les données dans une base **PostgreSQL**, dans une architecture modulaire et testable.

- Ajout d’un **DTO** (`BookDTO`) pour les échanges HTTP
- Création d’un **contrôleur REST** (`BookController`) avec deux routes :
  - `GET /books` : retourne les livres triés
  - `POST /books` : crée un nouveau livre (validation intégrée)
- Déclaration des beans de l’application avec `@Configuration` (ex. `BookUseCase`)
- Création d’un **adapter JDBC** (`BookDAO`) implémentant le port `BookRepositoryPort`
- Écriture de requêtes SQL via `NamedParameterJdbcTemplate`
- Gestion de la base avec **Liquibase** :
  - Changelog XML de création de la table `book`
  - Auto-exécution au lancement de l’application
- Base PostgreSQL lancée via **Docker Compose**

➡️ **Tests d’intégration** :
- Création d’un test pour le contrôleur avec **MockMVC** et **Spring Mockk**
- Gestion des cas nominaux, erreurs 400/500 et mapping DTO
- Création d’un test pour le DAO avec **Testcontainers**
  - Lancement automatique d’un conteneur PostgreSQL
  - Nettoyage de la table avant chaque test
  - Injection réelle du `BookDAO` via `@Autowired`

➡️ **CI/CD avec GitHub Actions** :
- Intégration de la suite de test d’intégration (`testIntegration`) dans Gradle
- Rapport de couverture **JaCoCo** incluant les tests d’intégration
- Pipeline GitHub Actions mis à jour :
  - Build + tests unitaires & intégration
  - Exécution de **PIT Mutation Testing**
  - Upload des rapports JaCoCo, tests et mutations

---

## 🧰 Technologies & outils utilisés

- **Kotlin** (langage principal)
- **Spring Boot** (structure du projet)
- **Kotest** (framework de test unitaire & property-based)
- **MockK** (mocks & stubs pour tests d'interaction)
- **Gradle** (build tool)
- **JaCoCo** (couverture de code)
- **PIT** (mutation testing)

---

## 🏫 Contexte

Projet réalisé dans le cadre du module `Méthodologie des tests` à **Ynov Bordeaux** – Mastère 2.  
Enseignements centrés sur les bonnes pratiques de tests logiciels, la qualité de code, et l’approche TDD.

---

## 📌 À faire

- [ ] Ajouter le TP3 : tests d’intégration ou architecture hexagonale
- [ ] Ajouter la CI/CD avec GitHub Actions
- [ ] Générer les rapports de couverture (JaCoCo)
- [ ] Ajouter des tests de mutation (PIT)
