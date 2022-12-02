package ru.job4j.todo.service;

import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAll();

    Optional<Category> findById(int id);

    Optional<Category> add(Category category);

    boolean update(Category category);

    boolean delete(Category category);

    boolean deleteById(int id);
}