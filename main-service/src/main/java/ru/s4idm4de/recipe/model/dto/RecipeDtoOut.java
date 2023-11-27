package ru.s4idm4de.recipe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s4idm4de.category.model.dto.CategoryDtoOut;
import ru.s4idm4de.user.model.dto.UserDtoOut;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDtoOut {
    private String title;

    private String text;

    private String video;

    private final List<CategoryDtoOut> categories = new ArrayList<>();

    private Boolean isPrivate;

    private final List<String> images = new ArrayList<>();

    private UserDtoOut author;

    private Long likes;

    private String createdAt;

    private String updatedAt;

    public void addImage(String link) {
        images.add(link);
    }

    public void addCategoryDtoOut(CategoryDtoOut categoryDtoOut) {
        categories.add(categoryDtoOut);
    }
}
