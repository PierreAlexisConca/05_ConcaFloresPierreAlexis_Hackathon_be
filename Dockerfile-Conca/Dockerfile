# ─────────────────────────────────────────────────────────────────────────────
# Dockerfile-Conca — Backend Spring Boot (Java 25)
# Imagen: concaflores/recuperacion_be:1.0
# Puerto: 8080
# ─────────────────────────────────────────────────────────────────────────────

# Stage 1: Build con Maven + Amazon Corretto 25
FROM maven:3.9-amazoncorretto-25-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


# Stage 2: Run con Amazon Corretto 25 (ligero)
FROM amazoncorretto:25-alpine

WORKDIR /app

# Instalar fuentes necesarias para Apache POI (Excel) e iText (PDF)
RUN apk add --no-cache \
    fontconfig \
    ttf-dejavu \
    font-noto

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]


# ─────────────────────────────────────────────────────────────────────────────
# CONSTRUIR Y SUBIR LA IMAGEN (ejecutar desde la raíz del proyecto BE)
#
# Construcción:
# docker build -t concaflores/recuperacion_be:1.0 -f Dockerfile-Conca/Dockerfile .
#
# Ejecución local (opcional):
# docker run -d --name recuperacion_be -p 8080:8080 concaflores/recuperacion_be:1.0
# Verificar: curl http://localhost:8080/api/promotores
#
# Subir a Docker Hub:
# docker push concaflores/recuperacion_be:1.0
#
# Descargar en AWS EC2:
# docker pull concaflores/recuperacion_be:1.0
# docker run -d --name recuperacion_be -p 8080:8080 \
#   -e SPRING_DATASOURCE_URL=jdbc:sqlserver://[SQL_IP]:1433;databaseName=ConcaFlores \
#   -e SPRING_DATASOURCE_USERNAME=sa \
#   -e SPRING_DATASOURCE_PASSWORD=[PASSWORD] \
#   concaflores/recuperacion_be:1.0
# ─────────────────────────────────────────────────────────────────────────────
