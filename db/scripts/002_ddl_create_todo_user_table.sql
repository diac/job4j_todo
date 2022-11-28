CREATE TABLE todo_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR UNIQUE,
    login VARCHAR,
    password VARCHAR
);