CREATE TABLE todo_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    login VARCHAR UNIQUE,
    password VARCHAR
);