# imagen base del JDK
FROM eclipse-temurin:21-jdk-alpine

# Crea un usuario que no tiene privilegios de root
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Información del mantenedor
LABEL maintainer="Komatsu Route Optimizer"

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR de la aplicación
COPY target/komatsurouteoptimizer-0.0.1-SNAPSHOT.jar app.jar

# Cambia la propiedad del archivo a no root
RUN chown appuser:appgroup app.jar

# Cambia al usuario que no tiene privilegios de root
USER appuser

# escucha el puerto 8080
EXPOSE 8080

# optimizar el rendimient JVM
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom"

# ejecuta la app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 