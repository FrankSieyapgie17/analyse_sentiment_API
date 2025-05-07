# Utiliser une image de base
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

ARG JAR_FILE=target/*.jar

# Copier le JAR dans le conteneur
COPY  ${JAR_FILE} app/myapp.jar

# Exposer le port de l'application
EXPOSE 8080

# Commande à exécuter
ENTRYPOINT ["java", "-jar", "app/myapp.jar"]