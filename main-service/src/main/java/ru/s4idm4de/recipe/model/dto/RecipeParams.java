package ru.s4idm4de.recipe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecipeParams {

    private Long userId;

    private Boolean isPrivate;

    private Integer from;

    private Integer size;

    private RecipeSort sort;

    private final List<Long> categoryIds = new ArrayList<>();

    public void setCategory(Long categoryId) {
        categoryIds.add(categoryId);
    }
}
