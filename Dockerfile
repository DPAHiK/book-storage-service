# Используем официальный образ OpenJDK
FROM openjdk:11-jre-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR файл в контейнер
COPY target/book-storage-service-0.0.1-SNAPSHOT.jar book-storage-service-0.0.1-SNAPSHOT.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "book-storage-service-0.0.1-SNAPSHOT.jar"]
