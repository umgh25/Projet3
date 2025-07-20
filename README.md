# 🏠 Chatop API

Application backend développée avec Spring Boot permettant de gérer les utilisateurs, les locations de biens immobiliers et les messages échangés sur la plateforme ChâTop.

## 📌 Présentation

Cette application backend offre une gestion complète des locations immobilières entre particuliers. Elle permet aux utilisateurs de s’inscrire, publier ou consulter des annonces de location, et d’échanger des messages via une API REST sécurisée. L’ensemble est conçu pour assurer une expérience fluide, fiable et sécurisée côté client.

## 🚀 Technologies utilisées

- ![Java](https://img.shields.io/badge/Java-21-red?logo=openjdk&logoColor=white)
- ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen?logo=spring&logoColor=white)
- ![MySQL](https://img.shields.io/badge/MySQL-8.5-blue?logo=mysql&logoColor=white)
- ![JWT](https://img.shields.io/badge/JWT-Security-yellow?logo=jsonwebtokens&logoColor=white)
- ![Swagger](https://img.shields.io/badge/Swagger-API-green?logo=swagger&logoColor=white)


## ⚙️ Installation et configuration

### ✅ Prérequis
- Java 21
- Maven
- MySQL 8.5
- Postman (optionnel, pour tester l'API)

### 🔧 Étapes d'installation

####  Cloner le projet
```bash
git clone https://github.com/umgh25/Projet3.git
cd Projet3/backend
```

####  Configurer la base de données
Connectez-vous à MySQL :
```bash
mysql -u root -p
```
Dans le shell MySQL, exécutez :
```sql
CREATE DATABASE chatop_db;
CREATE USER 'chatop'@'localhost' IDENTIFIED BY 'votre_mot_de_passe';
GRANT ALL PRIVILEGES ON chatop_db.* TO 'chatop'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```
Ensuite, exécutez le script SQL fourni :
```bash
mysql -u root -p chatop_db < frontend/ressources/sql/script.sql
```

####  Configurer les variables d'environnement
Créez un fichier `.env` à la racine du dossier `backend` et ajoutez :
```env
JWT_SECRET=votre_clé_secrète
SPRING_DATASOURCE_USERNAME=chatop
SPRING_DATASOURCE_PASSWORD=votre_mot_de_passe
```

####  Installer les dépendances
```bash
mvn clean install
```

####  Démarrer le serveur backend
```bash
mvn spring-boot:run
```
L'API sera accessible sur : [http://localhost:8080](http://localhost:8080)

####  Démarrer le frontend
```bash
cd Projet3/frontend
ng serve
```
L'application frontend sera accessible sur : [http://localhost:4200](http://localhost:4200)

---

## 📝 Documentation API
La documentation Swagger est disponible à l'adresse : [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## 🛠️ Tests
Vous pouvez utiliser Postman ou tout autre outil similaire pour tester les endpoints de l'API. Un fichier de collection Postman est disponible dans `frontend/ressources/postman/rental.postman_collection.json`.

## 📂 Structure du projet
- `backend/` : Code source du backend (API Spring Boot)
- `frontend/` : Code source du frontend (Angular)
