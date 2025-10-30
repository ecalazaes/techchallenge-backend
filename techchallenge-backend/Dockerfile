# Etapa 1: Escolha a imagem base do Maven para compilar o código Java
FROM maven:latest AS build

# Defina o diretório de trabalho para /app
WORKDIR /app

COPY pom.xml .
COPY src ./src

# Execute o comando para compilar e gerar o .jar
RUN mvn clean package

# Usar a imagem oficial do JDK 17
FROM openjdk:21-jdk-slim

# Criar um diretório para a aplicação
WORKDIR /app

# Copiar o arquivo JAR gerado para o container
COPY --from=build /app/target/*.jar app.jar

# Expor a porta utilizada
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]