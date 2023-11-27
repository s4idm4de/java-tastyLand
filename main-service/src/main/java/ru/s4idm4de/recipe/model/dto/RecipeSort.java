package ru.s4idm4de.recipe.model.dto;

public enum RecipeSort {
    LIKE("likes"),
    DATE("createdAt"),
    TITLE("title");

    private String name;

    RecipeSort(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
