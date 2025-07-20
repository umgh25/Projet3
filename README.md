# 🏠 Chatop API

## 📌 Description
Chatop API est le backend d'une application de gestion de locations. Elle permet la gestion des utilisateurs, des locations et des messages entre utilisateurs.

## 🚀 Technologies utilisées
- **Langage :** Java 21
- **Framework :** Spring Boot 3.4.3
- **Base de données :** MySQL 8.5
- **Authentification :** JWT (JSON Web Token)
- **Documentation API :** Swagger

## ⚙️ Installation et configuration

### ✅ Prérequis
- Java 21
- Maven
- MySQL 8.5
- Postman (optionnel, pour tester l'API)

### 🔧 Étapes d'installation

#### 1️⃣ Cloner le projet
```bash
git clone https://github.com/umgh25/Projet3.git
cd Projet3/backend
```

#### 2️⃣ Configurer la base de données
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

#### 3️⃣ Configurer les variables d'environnement
Créez un fichier `.env` à la racine du dossier `backend` et ajoutez :
```env
JWT_SECRET=votre_clé_secrète
SPRING_DATASOURCE_USERNAME=chatop
SPRING_DATASOURCE_PASSWORD=votre_mot_de_passe
```

#### 4️⃣ Installer les dépendances
```bash
mvn clean install
```

#### 5️⃣ Démarrer le serveur backend
```bash
mvn spring-boot:run
```
L'API sera accessible sur : [http://localhost:8080](http://localhost:8080)

#### 6️⃣ Démarrer le frontend
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
