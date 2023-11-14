package ru.s4idm4de.recipe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.s4idm4de.recipe.model.dto.RecipeParams;
import ru.s4idm4de.recipe.model.dto.RecipeDtoIn;
import ru.s4idm4de.recipe.model.dto.RecipeDtoOut;
import ru.s4idm4de.recipe.model.dto.RecipeSort;
import ru.s4idm4de.recipe.service.RecipeServiceUsers;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/recipes")
@Slf4j
@RequiredArgsConstructor
public class RecipeControllerUsers {

    @Autowired
    private final RecipeServiceUsers recipeServiceUsers;

    @GetMapping
    public List<RecipeDtoOut> getRecipes(@PathVariable Long userId,
                                         @RequestParam(name = "categories", required = false) List<Long> categoryIds,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                         @RequestParam(name = "sort", defaultValue = "DATE") RecipeSort sort) {
        RecipeParams recipeParams = RecipeParams.builder()
                .userId(userId)
                .sort(sort)
                .from(from)
                .size(size).build();
        categoryIds.forEach(recipeParams::setCategory);
        log.info("RecipeControllerUsers getRecipes recipeParams {}", recipeParams);
        return recipeServiceUsers.getRecipes(recipeParams);
    }

    @GetMapping(path = "/{recipeId}")
    public RecipeDtoOut getRecipe(@PathVariable Long userId,
                                  @PathVariable Long recipeId) {
        log.info("RecipeControllerUsers getRecipe userId {}, recipeId {}", userId, recipeId);
        return recipeServiceUsers.getRecipe(userId, recipeId);
    }

    @PostMapping
    public ResponseEntity<RecipeDtoOut> postRecipe(@PathVariable Long userId,
                                                   @RequestBody @Validated RecipeDtoIn recipeDtoIn) {
        log.info("RecipeControllerUsers postRecipe userId {}, recipeDtoIn {}", userId, recipeDtoIn);
        return new ResponseEntity(recipeServiceUsers.postRecipe(userId, recipeDtoIn), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{recipeId}")
    public RecipeDtoOut patchRecipe(@PathVariable Long userId,
                                    @PathVariable Long recipeId,
                                    @RequestBody RecipeDtoIn recipeDtoIn) {
        log.info("RecipeControllerUsers userId {}, recipeId {}, recipeDtoIn {}", userId, recipeId, recipeDtoIn);
        return recipeServiceUsers.patchRecipe(userId, recipeId, recipeDtoIn);
    }

}
