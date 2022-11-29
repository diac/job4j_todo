ALTER TABLE tasks
    ADD user_id INT NOT NULL CONSTRAINT user_id_fk REFERENCES todo_user(id);