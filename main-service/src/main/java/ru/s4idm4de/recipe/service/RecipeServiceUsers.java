package ru.s4idm4de.recipe.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.s4idm4de.category.model.Category;
import ru.s4idm4de.recipe.model.dto.RecipeParams;
import ru.s4idm4de.category.repository.CategoryRepository;
import ru.s4idm4de.exception.ContradictionException;
import ru.s4idm4de.exception.NotFoundException;
import ru.s4idm4de.exception.ValidatedException;
import ru.s4idm4de.recipe.model.Image;
import ru.s4idm4de.recipe.model.QRecipe;
import ru.s4idm4de.recipe.model.Recipe;
import ru.s4idm4de.recipe.model.dto.RecipeDtoIn;
import ru.s4idm4de.recipe.model.dto.RecipeDtoOut;
import ru.s4idm4de.recipe.model.mapper.RecipeMapper;
import ru.s4idm4de.recipe.repository.ImageRepository;
import ru.s4idm4de.recipe.repository.RecipeRepository;
import ru.s4idm4de.user.UserRepository;
import ru.s4idm4de.user.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RecipeServiceUsers {

    @Autowired
    private final RecipeRepository recipeRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ImageRepository imageRepository;

    public List<RecipeDtoOut> getRecipes(RecipeParams recipeParams) {
        List<Recipe> recipes = recipeRepository.findAllRecipesByCategoriesWithParams(recipeParams.getCategoryIds(), recipeParams.getUserId(),
                PageRequest.of(recipeParams.getFrom() / recipeParams.getSize(), recipeParams.getSize(),
                        Sort.by(Sort.Direction.DESC, recipeParams.getSort().toString())));
        return RecipeMapper.toRecipeDtoOut(recipes);
    }

    public RecipeDtoOut getRecipe(Long userId, Long recipeId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NotFoundException("no user with id " + userId)
            );
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(
                    () -> new NotFoundException("no recipe with id " + recipeId)
            );
            if (recipe.getIsPrivate() && !recipe.getAuthor().equals(user)) {
                throw new ContradictionException("recipe " + recipeId + " is private and user " + userId + " is not an author");
            }
            return RecipeMapper.toRecipeDtoOut(recipe);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ContradictionException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    public RecipeDtoOut postRecipe(Long userId, RecipeDtoIn recipeDtoIn) {
        try {
            Recipe recipe = RecipeMapper.toRecipe(recipeDtoIn);
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NotFoundException("no user with id " + userId));
            if (!user.getAllowToPublish()) throw new ContradictionException("user " + userId + " can't publish");
            recipe.setAuthor(user);
            //recipe.setCategories(new HashSet<>());
            recipeDtoIn.getCategoryIds().forEach(categoryId -> addCategory(categoryId, recipe));
            recipeDtoIn.getImages().forEach(link -> addImage(link, recipe));
            return RecipeMapper.toRecipeDtoOut(recipeRepository.save(recipe));
        } catch (NotFoundException | ContradictionException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    //пока менять можно в любое время
    public RecipeDtoOut patchRecipe(Long userId, Long recipeId, RecipeDtoIn recipeDtoIn) {
        try {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NotFoundException("no user with id " + userId)
            );
            if (!user.getAllowToPublish()) throw new ContradictionException("user " + userId + " can't publish");
            Recipe oldRecipe = recipeRepository.findById(recipeId).orElseThrow(
                    () -> new NotFoundException("no recipe with id " + recipeId)
            );
            if (!user.equals(oldRecipe.getAuthor())) {
                throw new ValidatedException("user " + user + " not the author of recipe " + oldRecipe);
            }
            Recipe updatedRecipe = RecipeMapper.updateRecipe(oldRecipe, recipeDtoIn);
            if (recipeDtoIn.getCategoryIds() != null && !recipeDtoIn.getCategoryIds().isEmpty()) {
                recipeDtoIn.getCategoryIds().forEach(categoryId -> addCategory(categoryId, updatedRecipe));
            } else {
                oldRecipe.getCategories().forEach(updatedRecipe::addCategory);
            }
            if (recipeDtoIn.getImages() != null && !recipeDtoIn.getImages().isEmpty()) {
                oldRecipe.getImages().forEach(imageRepository::delete);
                recipeDtoIn.getImages().forEach(link -> addImage(link, updatedRecipe));
            } else {
                oldRecipe.getImages().forEach(updatedRecipe::addImage);
            }
            return RecipeMapper.toRecipeDtoOut(recipeRepository.save(updatedRecipe));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatedException | ContradictionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    private void addCategory(Long categoryId, Recipe recipe) {
        try {
            Category category = categoryRepository.findById(categoryId).orElseThrow(
                    () -> new NotFoundException("no category with id " + categoryId)
            );
            if (category.getIsPrivate() && !category.getAuthor().equals(recipe.getAuthor()))
                throw new ContradictionException("category " + categoryId + " is private and not of recipe author");
            recipe.addCategory(category);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ContradictionException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    private void addImage(String link, Recipe recipe) {
        Image image = Image.builder().link(link).build();
        Image imageWithId = imageRepository.save(image);
        recipe.addImage(imageWithId);
    }
}
