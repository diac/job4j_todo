# job4j_todo

## Описание проекта

Учебный проект для раздела Hibernate курса Job4J: Middle.

Проект представляет собой реализацию приложения по управлению задачами (TODO).
Пользователь может создавать, просматривать, редиктировать задачи, 
а также менять их статус (выполнено, не выполнено и т.п.).

## Стек технологий
- Java 17
- Spring Boot 2.7.3
- PostgreSQL 14.5
- Hibernate 5.6.11
- Thymeleaf 3.0.15
- Boostrap 4.4.1
- Liquibase 4.15.0

## Требования к окружению
- Java 17+
- Maven 3.8
- PostgreSQL 14

### Создание схемы БД
```shell
CREATE DATABASE job4j_todo
```

### Инициализация БД
Сначала необходимо настроить подключение Liquibase к созданной базе данных.<br>
Для этого папке проекта /db создать файл liquibase.properties, в котором указать параметры подключения к базе данных.<br>
В качестве примера можно использовать файл liquibase.properties.example.<br>
Пример содержимого файла настроек liquibase.properties:<br>
```
changeLogFile: db/master.xml
url: jdbc:postgresql://hostname:port/db_name
username: db_username
password: db_password
```


Затем, командной строке перейти в папку проекта и запустить команду<br>
```shell
mvn liquibase:update
```

### Настройка подключения к БД
В папке проекта /src/main/resources создать файл hibernate.cfg.xml, в котором указать параметры подключения к базе данных.<br>
В качестве примера можно использовать файл hibernate.cfg.xml.example.<br>
Пример содержимого файла настроек hibernate.cfg.xml:<br>
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
        <property name="hibernate.connection.url">jdbc:postgresql://hostname:port/db_name</property>
        <property name="hibernate.connection.username">db_username</property>
        <property name="hibernate.connection.password">db_password</property>
        <property name="hibernate.connection.pool_size">1</property>
        <property name="hibernate.current_session_context_class">thread</property>
        <property name="hibernate.show_sql">true</property>
        <property name="hibernate.dialect">org.hibernate.dialect.PostgreSQLDialect</property>

        <mapping class="ru.job4j.todo.model.Task" />
        <mapping class="ru.job4j.todo.model.User" />
    </session-factory>
</hibernate-configuration>
```

## Запуск приложения
```shell
mvn spring-boot:run
```

## Взаимодействие с приложением
### Список всех задач
![Список всех задач](/img/001_tasks_index.png)
На этой странице отображается список всех задач. Список можно отфильтровать по критерию "Выполненные" или "Новые".

### Создание новой задачи
![Создание новой задачи](/img/002_new_task.png)

### Просмотр задачи
![Просмотр задачи](/img/003_task_view.png)
На этой странице доступно три действия:
- Пометить задачу как выполненную (кнопка "Выполнено")
- Перейти на страницу редактирования задачи (кнопка "Отредактировать")
- Удалить задачу (кнопка "Удалить")

### Редактирование задачи
![Редактирование задачи](/img/004_task_edit.png)

## Контакты
email: nikolai.gladkikh.biz22@gmail.com