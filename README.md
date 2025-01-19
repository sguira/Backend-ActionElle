
## 1. Titre: Backend Action"Elle 

## Contextexte Du projet
Cette Action a été réalisation dans le cadre d'un test technique pour le poste de développeur d'application au sein de Africa Devolution. L'application est la partie Back-end d'une application qui permet aux femmes d'une ONG d'être des pouvoyeuses d'affaires pour une assurance en calculant des défis pour des souscripteurs. L'application à été réaliser avec le Framework **Spring Boot**

## 2. **Description**
Backend Action"Elle est une application Backend qui est une interface entre l'application Front <a href="https://github.com/sguira/front-action-elle.git">Action"Elle</a> et la source de donnée. Cette application mets en place l'ensemble des méthode qui permette de manipuler les données dans cette Application 
Pour cette application nous avons deux principales utilisateurs qui sont les 
- utilisateurs Simple (Amazone) 
- Administrateur
J'utilise la base de donnée MongoDb pour la persistance des données.

## 3. Prérequis
- Java 11
- Maven (Pour la gestion des dépendances) <br>
Pour la mise en place de notre application Nous Utilisons les dependances suivantes:
    - Spring Boot 2.5.3
    - Spring Security 5.4.4
    - Mongo 
    - Lombok
    - Swagger 2.9.2
    - Spring Data JPA 2.5.3
## 4. Mise en place de l'application
Afin d'éxecuter l'application vous pouvez utiliser deux approches distinctes 
- **Approche 1** : **Approche Classique**
- Cloner le projet `git clone https://github.com/sguira/front-action-elle.git`
- Ouvrir le projet dans votre IDE (IntelliJ, Eclipse, Netbeans, etc) ` code .`
- Ouvrir le terminal et se positionner dans le dossier du projet
- Lancer l'application avec mvn 
 : `mvn spring`
- **Approche 2** : **Approche Docker**
- Cloner le projet
- Ouvrir le terminal et se positionner dans le dossier du projet
- S'assurer que Docker engine est en marche 
- **Crée votre container avec Dockerfile**
- `docker build -t action-elle .`
- Lancer Le container 
- `docker run -p 8080:8080 action-elle`

Le projet sera transmit avec un fichier docker-compose qui permet de lancer de manière structuré les deux ccontainers pour faciliter l'execution de l'environnement des application


## 4. Spécification de Sécurité 

## 4.1 Spring Securité: 
Pour sécuriser les réquêtes de notre application nous utilisons Spring sécurity qui est un composant qu'on ajoute aux applications spring pour améliorer la securité d'une application. <br><br>
- **Bearer Authentification:** Toutes les requêtes vers l'application Rest doivent être accompagné d'un token d'authorization, le token est crée après l'authentification de l'utilisateur et permet à l'utilisateur d'accéder à ces données sur une durée de 7jours 

- **Rate Limiting:** Pour limiter le nombre de requêtes par utilisateur à 200 par minute, nous avons utilisé le package **spring-boot-starter-cafe et et Cafeine** qui permet de stocker le nombre de requêtes par utilisateur selon une durée donnée dans le Cache Caféine.

- **JWT:** Pour la création du token d'authentification nous avons utilisé le package **jjwt** qui permet de créer un token d'authentification. 
## 5. Swagger:
Pour la documentation de notre application nous avons utilisé Swagger qui est un outil qui permet de générer une documentation de l'application. <br>
Pour accéder à la documentation de l'application vous pouvez utiliser l'url suivante: `http://localhost:8080/swagger-ui/`. Pour des souscis de sécurité nous n'avons pas exposer l'api de l'administrateur dans la documentation.

## 6. Api de Base
- **GET /api/v1/subscriptions** : Récupérer la liste des souscriptions effectuées par une amazone spécifique (ajouter le token d'authentification comme en-tête à toutes les réquètes)
- **GET /api/v1/subscriptions/{id}** : Récupérer les détails d'une souscription 
- **POST /api/v1/subscriptions** : Créer une nouvelle souscription
- **PUT /api/v1/subscriptions/statut/id** Permet de modifier le statut d'un assuré de PROSPECT à CLIENT
- **GET /api/v1/subscriptions/id/attestation** Permet de crée une attestation pour une souscription donnée.

- **GET /api/v1/simulations** Retourne la liste des simulations pour une amazone donnée
- **GET /api/v1/simulations/{id}** Retourne les détails
- **POST /api/v1/simulations** Créer une nouvelle simulation

## 6. Utilisation de l'Api Rest
Pour tirer pleinement des fonctionnalité de base de cette application nous vous suggérons de l'utiliser avec la seconde partie qui répresente l'interface graphique pour les utilisateurs.





