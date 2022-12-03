package ru.job4j.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskFormDto {

    private int id;

    private String description;

    private int priorityId;

    private int[] categoryIds;
}