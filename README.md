# 🧪 Méthodologie des Tests - Ynov Bordeaux

Ce dépôt regroupe les différents travaux pratiques réalisés dans le cadre du cours **Méthodologie de tests et tests unitaires**, dispensé au sein du **Mastère 2 à Ynov Bordeaux**.

> 🔖 **Travail noté réalisé en groupe** :  
> **Ajout d’un système de réservation de livre**, développé par **Melina Mitterrand**, **Hugon Maurane** et **Dessauw Hugo**.  
> Ce travail s'appuie sur une architecture hexagonale, avec tests unitaires, d’intégration, et gestion de la persistance PostgreSQL.

---

## 🎯 Objectifs pédagogiques du module

- Apprendre à utiliser différents types de tests (unitaires, d'intégration, de mutation, etc.)
- Apprendre à écrire du code testable, maintenable et vérifiable
- Découvrir les outils de testing (Kotest, MockK, JaCoCo, PIT, etc.)
- Mettre en pratique le Test Driven Development (TDD) et le Property-Based Testing

---

## 🗂 Structure du projet

Ce dépôt contient plusieurs TP organisés par dossier, chacun illustrant un aspect particulier de la méthodologie de test :

### 📁 TP1 - Chiffrement de César
Objectif : Implémenter l’algorithme du chiffrement de César en TDD.
- Fonction `cipher(char: String, key: Int)`
- Gestion des cas limites : dépassement de Z, clés > 26, etc.
- Tests unitaires et property-based avec Kotest

### 📁 TP2 - Gestion de livres (partie métier)
Objectif : Implémenter la logique métier d’un gestionnaire de bibliothèque.
- Entité `Book`, port de persistance, règles métier
- Tri des livres, validation des champs
- Tests unitaires, MockK, Property-Based Testing

### 📁 TP3 - Intégration Web & Base de Données ✅ **(ACCOMPLI)**
Objectif : Exposer le domaine métier via une API REST et PostgreSQL.
- API REST avec `BookController`, DTO, mapping
- Persistance via `BookDAO` avec JDBC + Liquibase
- Tests d’intégration :
  - `BookControllerTest` (MockMVC + Mockk)
  - `BookDAOTest` (Testcontainers + PostgreSQL réel)
- Réservation de livres implémentée avec règles métier :
  - Impossibilité de réserver deux fois
  - Validation de l’existence du livre
  - Ajout de champ `isReserved` en base + DTO + contrôleur

---

## ⚙️ CI/CD avec GitHub Actions

- ✅ Build de l’application
- ✅ Exécution des tests (unitaires + intégration)
- ✅ Génération des rapports de couverture via **JaCoCo**
- ✅ Tests de mutation avec **PIT**
- ✅ Pipeline automatisé sur chaque commit/push

---

## 🧰 Technologies & outils utilisés

- **Kotlin** (langage principal)
- **Spring Boot** (framework applicatif)
- **Kotest** (tests unitaires + property-based)
- **MockK** (mocks, stubs)
- **Gradle** (build tool)
- **Liquibase** (migrations de base)
- **Testcontainers** (base PostgreSQL en test réel)
- **JaCoCo** (couverture)
- **PIT** (mutation testing)

---

## 📌 À faire

- [x] Ajouter le TP3 : tests d’intégration & architecture hexagonale
- [x] Ajouter la CI/CD avec GitHub Actions
- [x] Générer les rapports de couverture (JaCoCo)
- [x] Ajouter des tests de mutation (PIT)
