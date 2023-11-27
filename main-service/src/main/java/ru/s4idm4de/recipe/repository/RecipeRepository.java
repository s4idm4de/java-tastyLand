package ru.s4idm4de.recipe.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.s4idm4de.recipe.model.Recipe;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, QuerydslPredicateExecutor<Recipe> {

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.categories c WHERE c.id in ?1 " +
            "AND (c.isPrivate = FALSE or c.author.id = ?2) AND " +
            "(r.isPrivate = FALSE or r.author.id = ?2)")
    List<Recipe> findAllRecipesByCategoriesWithParams(List<Long> categoryIds, Long userId, PageRequest pageRequest);

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.categories c WHERE c.id in ?1 " +
            "AND (c.isPrivate = FALSE or c.author.id = ?2) AND " +
            "(r.isPrivate = FALSE or r.author.id = ?2)")
    List<Recipe> findAllRecipesByCategoriesWithParams(List<Long> categoryIds, Long userId);


}
