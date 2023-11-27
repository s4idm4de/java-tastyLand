package ru.s4idm4de.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.s4idm4de.recipe.model.LikeRecipe;
import ru.s4idm4de.recipe.model.Recipe;
import ru.s4idm4de.recipe.model.RecipeWithLikes;
import ru.s4idm4de.user.model.User;

import java.util.List;

public interface LikeRecipeRepository extends JpaRepository<LikeRecipe, Long> {

    LikeRecipe findByUserAndRecipe(User user, Recipe recipe);

    @Query("SELECT count(*) FROM LikeRecipe lr WHERE lr.recipe = ?1 GROUP BY lr.recipe")
    Long countLikesForRecipe(Recipe recipe);

    @Query(value = "SELECT new ru.s4idm4de.recipe.model.RecipeWithLikes(lr.recipe, count(*)) FROM LikeRecipe lr WHERE lr.recipe in ?1 GROUP BY lr.recipe")
    List<RecipeWithLikes> countLikesForRecipes(List<Recipe> recipes);
}
