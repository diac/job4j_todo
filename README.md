# job4j_todo

## Описание проекта

Учебный проект для раздела Hibernate курса Job4J: Middle.

Проект представляет собой реализацию приложения по управлению задачами (TODO).
Пользователь может создавать, просматривать, редиктировать задачи, 
а также менять их статус (выполнено, не выполнено и т.п.).

## Стек технологий
- Java 17
- Spring Boot
- JDBC
- PostgreSQL
- Hibernate
- Thymeleaf
- Boostrap
- Liquibase

## Требования к окружению
- Java 17+
- Maven 3.8
- PostgreSQL 14

### Создание схемы БД
```CREATE DATABASE job4j_todo```

### Инициализация БД
В командной строке перейти в папку проекта и запустить команду<br>
```mvn liquibase:update```

### Настройка подключения к БД
В папке проекта /src/main/resources создать файл db.properties, в котором указать параметры подкдючения к базе данных.<br>
В качестве примера можно использовать файл db.properties.example.<br>
Пример содержимого файла настроек db.properties:<br>
```
jdbc.url=jdbc:postgresql://hostname:port/db_name
jdbc.username=db_username
jdbc.password=db_password
jdbc.driver=org.postgresql.Driver
```

## Запуск приложения
```mvn spring-boot:run```

## Взаимодействие с приложением
TODO

## Контакты
email: nikolai.gladkikh.biz22@gmail.com