
## 1. Titre: Backend Action"Elle 

## Contextexte Du projet
Cette Action a été réalisation dans le cadre d'un test technique pour le poste de développeur d'application au sein de Africa Devolution. L'application est la partie Back-end d'une application qui permet aux femmes d'une ONG d'être des pouvoyeuses d'affaires pour une assurance en calculant des défis pour des souscripteurs. L'application à été réaliser avec le Framework **Spring Boot**

## 2. **Description**
Backend Action"Elle est une application Backend qui est une interface entre l'application Front <a href="https://github.com/sguira/front-action-elle.git">Action"Elle</a> et la source de donnée. Cette application mets en place l'ensemble des méthode qui permette de manipuler les données dans cette Application 
Pour cette application nous avons deux principales utilisateurs qui sont les 
- utilisateurs Simple (Amazone)
- Administrateur

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


