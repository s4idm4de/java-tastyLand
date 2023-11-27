package ru.s4idm4de.recipe.model.mapper;

import ru.s4idm4de.category.model.mapper.CategoryMapper;
import ru.s4idm4de.recipe.model.Recipe;
import ru.s4idm4de.recipe.model.RecipeWithLikes;
import ru.s4idm4de.recipe.model.dto.RecipeDtoIn;
import ru.s4idm4de.recipe.model.dto.RecipeDtoOut;
import ru.s4idm4de.user.model.mapper.UserMapper;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RecipeMapper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Recipe toRecipe(RecipeDtoIn recipeDtoIn) {
        return Recipe.builder()
                .text(recipeDtoIn.getText())
                .title(recipeDtoIn.getTitle())
                .video(recipeDtoIn.getVideo())
                .createdAt(LocalDateTime.now())
                .isPrivate(recipeDtoIn.getIsPrivate()).build();
    }

    public static RecipeDtoOut toRecipeDtoOut(Recipe recipe) {
        RecipeDtoOut recipeDtoOut = RecipeDtoOut.builder()
                .text(recipe.getText())
                .title(recipe.getTitle())
                .video(recipe.getVideo())
                .likes(recipe.getLikes())
                .createdAt(recipe.getCreatedAt().format(formatter))
                .updatedAt(recipe.getUpdatedAt() == null ? null : recipe.getUpdatedAt().format(formatter))
                .author(UserMapper.toUserDtoOut(recipe.getAuthor()))
                .isPrivate(recipe.getIsPrivate()).build();
        recipe.getCategories().forEach(category ->
                recipeDtoOut.addCategoryDtoOut(CategoryMapper.toCategoryDtoOut(category))
        );
        recipe.getImages().forEach(image ->
                recipeDtoOut.addImage(image.getLink())
        );
        return recipeDtoOut;
    }

    public static List<RecipeDtoOut> toRecipeDtoOut(Iterable<Recipe> recipes) {
        List<RecipeDtoOut> result = new ArrayList<>();
        for (Recipe recipe : recipes) {
            result.add(toRecipeDtoOut(recipe));
        }
        return result;
    }

    public static Recipe updateRecipe(Recipe recipe, RecipeDtoIn recipeDtoIn) {
        @Valid Recipe newRecipe = Recipe.builder()
                .text(recipeDtoIn.getText() == null ? recipe.getText() : recipeDtoIn.getText())
                .title(recipeDtoIn.getTitle() == null ? recipe.getTitle() : recipeDtoIn.getTitle())
                .video(recipeDtoIn.getVideo() == null ? recipe.getVideo() : recipeDtoIn.getVideo())
                .createdAt(recipe.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .isPrivate(recipeDtoIn.getIsPrivate())
                .id(recipe.getId())
                //.categories(new HashSet<>())
                .author(recipe.getAuthor())
                //.images(recipe.getImages())
                .build();
        return newRecipe;
    }

    public static List<Recipe> toRecipe(Iterable<RecipeWithLikes> recipesWithLikes) {
        List<Recipe> recipes = new ArrayList<>();
        for (RecipeWithLikes recipeWithLikes : recipesWithLikes) {
            recipeWithLikes.getRecipe().setLikes(recipeWithLikes.getLikes());
            recipes.add(recipeWithLikes.getRecipe());
        }
        return recipes;
    }
}
