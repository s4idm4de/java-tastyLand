package ru.s4idm4de.testRecipe;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;
import ru.s4idm4de.category.controller.CategoryController;
import ru.s4idm4de.category.model.Category;
import ru.s4idm4de.category.model.RequestCategory;
import ru.s4idm4de.category.model.dto.CategoryDtoIn;
import ru.s4idm4de.category.model.dto.CategoryDtoOut;
import ru.s4idm4de.category.model.dto.ConfirmedCategoriesDto;
import ru.s4idm4de.recipe.controller.RecipeControllerUsers;
import ru.s4idm4de.recipe.model.Image;
import ru.s4idm4de.recipe.model.Recipe;
import ru.s4idm4de.recipe.model.dto.RecipeDtoIn;
import ru.s4idm4de.recipe.model.dto.RecipeDtoOut;
import ru.s4idm4de.recipe.model.dto.RecipeSort;
import ru.s4idm4de.user.UserController;
import ru.s4idm4de.user.model.Gender;
import ru.s4idm4de.user.model.User;
import ru.s4idm4de.user.model.dto.UserDtoIn;
import ru.s4idm4de.user.model.dto.UserDtoOut;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "db.name=test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecipeTest {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EntityManager em;
    private UserDtoIn userDtoIn1;

    private UserDtoIn userDtoIn2;

    private User user1;

    private User user2;

    private UserDtoOut userDtoOut1;

    private UserDtoOut userDtoOut2;

    private CategoryDtoIn categoryDtoIn1;

    private CategoryDtoIn categoryDtoIn2;

    private CategoryDtoIn categoryDtoIn3;

    private CategoryDtoOut categoryDtoOut1;

    private CategoryDtoOut categoryDtoOut2;

    private CategoryDtoOut categoryDtoOut3;

    private Category category1;

    private Category category2;

    private Category category3;

    private Category category4;

    private RequestCategory requestCategory2;

    private ConfirmedCategoriesDto confirmedCategoriesDto;

    private final UserController userController;

    private final CategoryController categoryController;

    private final RecipeControllerUsers recipeControllerUsers;

    private RecipeDtoIn recipeDtoIn1;

    private RecipeDtoIn recipeDtoIn2;

    private RecipeDtoIn recipeDtoIn3;

    private Recipe recipe1;

    private Recipe recipe2;

    private Recipe recipe3;

    private RecipeDtoOut recipeDtoOut1;

    private RecipeDtoOut recipeDtoOut2;

    private RecipeDtoOut recipeDtoOut3;

    private Image image1;

    private Image image2;

    private Image image3;

    private Image image4;

    private Image image5;

    private Image image6;

    private Image image7;

    private Image image8;

    @BeforeEach
    void beforeEach() {
        userDtoIn1 = UserDtoIn.builder().email("user1@mail.ru")
                .gender(Gender.FEMALE)
                .birthday(LocalDate.of(1998, 7, 2))
                .name("user1").build();
        userDtoIn2 = UserDtoIn.builder().email("user2@mail.ru")
                .gender(Gender.FEMALE)
                .birthday(LocalDate.of(1998, 7, 2))
                .name("user2").build();
        user1 = User.builder().email("user1@mail.ru")
                .id(1L)
                .gender(Gender.FEMALE)
                .allowToPublish(true)
                .birthday(LocalDate.of(1998, 7, 2))
                .name("user1").build();
        user2 = User.builder().email("user2@mail.ru")
                .id(2L)
                .gender(Gender.FEMALE)
                .allowToPublish(true)
                .birthday(LocalDate.of(1998, 7, 2))
                .name("user2").build();
        userDtoOut1 = UserDtoOut.builder()
                .email("user1@mail.ru")
                .name("user1")
                .gender(Gender.FEMALE)
                .age(25)
                .build();
        userDtoOut2 = UserDtoOut.builder()
                .email("user2@mail.ru")
                .name("User2")
                .gender(Gender.FEMALE)
                .age(25)
                .build();
        userController.postUser(userDtoIn1);
        userController.postUser(userDtoIn2);
        categoryDtoIn1 = CategoryDtoIn.builder().name("category1").isPrivate(true).build();
        categoryDtoIn2 = CategoryDtoIn.builder().name("category2").isPrivate(true).build();
        categoryDtoIn3 = CategoryDtoIn.builder().name("category3").isPrivate(true).build();
        categoryDtoOut1 = CategoryDtoOut.builder().name("category1").isPrivate(true).build();
        categoryDtoOut2 = CategoryDtoOut.builder().name("category2").isPrivate(true).build();
        categoryDtoOut3 = CategoryDtoOut.builder().name("category3").isPrivate(true).build();
        category1 = Category.builder().id(1L).isPrivate(true).name("category1").author(user1).build();
        category2 = Category.builder().id(2L).isPrivate(true).name("category2").author(user1).build();
        category3 = Category.builder().id(3L).isPrivate(true).name("category3").author(user2).build();
        category4 = Category.builder().id(4L).isPrivate(false).name("category4").author(user1).build();
        confirmedCategoriesDto = ConfirmedCategoriesDto.builder().confirmedCategories(new HashMap<>(){{
            put("category4", 1L);
            put("category5", 1L);
        }}).build();
        requestCategory2 = RequestCategory.builder().createdAt(LocalDateTime.now())
                .id(2L)
                .name("category2")
                .initiator(user1).build();
        categoryController.postCategory(1L, categoryDtoIn1);
        categoryController.postCategory(1L, categoryDtoIn2);
        categoryController.postCategory(2L, categoryDtoIn3);
        categoryController.confirmCategories(1L, confirmedCategoriesDto);
        //recipe
        image1 = Image.builder().link("image1").id(1L).build();
        image2 = Image.builder().link("image2").id(2L).build();
        image3 = Image.builder().link("image3").id(3L).build();
        image4 = Image.builder().link("image4").id(4L).build();
        image5 = Image.builder().link("image5").id(5L).build();
        image6 = Image.builder().link("image6").id(6L).build();
        image7 = Image.builder().link("image7").id(7L).build();
        image8 = Image.builder().link("image8").id(8L).build();
        recipeDtoIn1 = RecipeDtoIn.builder()
                .categoryIds(List.of(1L, 2L))
                .images(List.of("image1", "image2", "image3"))
                .isPrivate(false)
                .text("text")
                .title("title1")
                .video("video")
                .build();
        recipe1 = Recipe.builder()//.categories(Set.of(category1, category2))
                //.images(Set.of(image1, image2, image3))
                .title("title1")
                .text("text")
                .video("video")
                .author(user1)
                .isPrivate(false)
                .id(1L)
                .createdAt(LocalDateTime.now()).build();
        recipe1.addImage(image1);
        recipe1.addImage(image2);
        recipe1.addImage(image3);
        recipe1.addCategory(category1);
        recipe1.addCategory(category2);
        recipeDtoOut1 = RecipeDtoOut.builder()
                .isPrivate(false)
                .likes(0L)
                .text("text")
                .video("video")
                .author(userDtoOut1)
                .createdAt(LocalDateTime.now().format(formatter))
                //.categories(List.of(categoryDtoOut1, categoryDtoOut2))
                //.images(List.of(image1.getLink(), image2.getLink(), image3.getLink()))
                .build();
        recipeDtoIn2 = RecipeDtoIn.builder()
                .categoryIds(List.of(2L))
                .images(List.of("image4", "image5"))
                .isPrivate(true)
                .text("text")
                .title("recipe2")
                .video("video")
                .build();
        recipe2 = Recipe.builder()//.categories(Set.of(category2))
                //.images(Set.of(image2, image3))
                .title("recipe2")
                .text("text")
                .video("video")
                .author(user1)
                .isPrivate(true)
                .id(2L)
                .createdAt(LocalDateTime.now()).build();
        recipe2.addCategory(category2);
        recipe2.addImage(image5);
        recipe2.addImage(image4);
        recipeDtoOut2 = RecipeDtoOut.builder()
                .isPrivate(true)
                .title("recipe2")
                .text("text")
                .video("video")
                .author(userDtoOut1)
                .createdAt(LocalDateTime.now().format(formatter))
                //.categories(List.of(categoryDtoOut2))
                //.images(List.of(image2.getLink(), image3.getLink()))
                .build();
        recipeDtoOut2.addCategoryDtoOut(categoryDtoOut2);
        recipeDtoOut2.addImage(image5.getLink());
        recipeDtoOut2.addImage(image4.getLink());
        recipeDtoIn3 = RecipeDtoIn.builder()
                .categoryIds(List.of(3L, 4L))
                .images(List.of("image6", "image7", "image8"))
                .isPrivate(false)
                .text("text")
                .title("title3")
                .video("video")
                .build();
        recipe3 = Recipe.builder()//.categories(Set.of(category1, category2))
                //.images(Set.of(image1, image2, image3))
                .title("title3")
                .text("text")
                .video("video")
                .author(user2)
                .isPrivate(false)
                .id(3L)
                .createdAt(LocalDateTime.now()).build();
        recipe3.addImage(image6);
        recipe3.addImage(image7);
        recipe3.addImage(image8);
        recipe3.addCategory(category4);
        recipe3.addCategory(category3);
        recipeDtoOut3 = RecipeDtoOut.builder().isPrivate(false)
                .text("text")
                .video("video")
                .author(userDtoOut1)
                .createdAt(LocalDateTime.now().format(formatter))
                //.categories(List.of(categoryDtoOut1, categoryDtoOut2))
                //.images(List.of(image1.getLink(), image2.getLink(), image3.getLink()))
                .build();
    }

    @Test
    void postRecipe() {
        recipeControllerUsers.postRecipe(1L, recipeDtoIn1);
        TypedQuery<Recipe> query = em.createQuery("Select r from Recipe r where r.id = :id", Recipe.class);
        Recipe recipe = query.setParameter("id", 1L)
                .getSingleResult();
        recipe1.setCreatedAt(recipe.getCreatedAt());
        assertEquals(recipe1, recipe);
        recipeControllerUsers.postRecipe(1L, recipeDtoIn2);
        TypedQuery<Recipe> query2 = em.createQuery("Select r from Recipe r where r.id = :id", Recipe.class);
        Recipe recipeTwo = query2.setParameter("id", 2L)
                .getSingleResult();
        recipe2.setCreatedAt(recipeTwo.getCreatedAt());
        assertEquals(recipe2, recipeTwo);
        recipeControllerUsers.postRecipe(2L, recipeDtoIn3);
        TypedQuery<Recipe> query3 = em.createQuery("Select r from Recipe r where r.id = :id", Recipe.class);
        Recipe recipeThree = query3.setParameter("id", 3L)
                .getSingleResult();
        recipe3.setCreatedAt(recipeThree.getCreatedAt());
        assertEquals(recipe3, recipeThree);
    }

    @Test
    void patchRecipe() {
        recipeControllerUsers.postRecipe(1L, recipeDtoIn1);
        recipeControllerUsers.postRecipe(1L, recipeDtoIn2);
        recipeControllerUsers.postRecipe(2L, recipeDtoIn3);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws ResponseStatusException {
                recipeControllerUsers.patchRecipe(2L, 1L, recipeDtoIn1);
            }
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        recipeDtoIn1.setImages(null);
        recipeControllerUsers.patchRecipe(1L, 1L, recipeDtoIn1);
        TypedQuery<Recipe> query = em.createQuery("Select r from Recipe r where r.id = :id", Recipe.class);
        Recipe recipe = query.setParameter("id", 1L)
                .getSingleResult();
        recipe1.setCreatedAt(recipe.getCreatedAt());
        recipe1.setUpdatedAt(recipe.getUpdatedAt());
        assertEquals(recipe1, recipe);
        Image newImage = Image.builder().id(9L).link("link9").build();
        recipeDtoIn1.setImages(List.of("link9"));
        recipeControllerUsers.patchRecipe(1L, 1L, recipeDtoIn1);
        TypedQuery<Recipe> query2 = em.createQuery("Select r from Recipe r where r.id = :id", Recipe.class);
        Recipe recipeTwo = query2.setParameter("id", 1L)
                .getSingleResult();
        Recipe recipeNew = Recipe.builder()//.categories(Set.of(category1, category2))
                //.images(Set.of(image1, image2, image3))
                .title("title1")
                .text("text")
                .video("video")
                .author(user1)
                .isPrivate(false)
                .id(1L)
                .createdAt(LocalDateTime.now()).build();
        recipeNew.addCategory(category1);
        recipeNew.addCategory(category2);
        recipeNew.setCreatedAt(recipeTwo.getCreatedAt());
        recipeNew.setUpdatedAt(recipeTwo.getUpdatedAt());
        recipeNew.addImage(newImage);
        assertEquals(recipeNew, recipeTwo);
    }

    @Test
    void getRecipe() {
        recipeControllerUsers.postRecipe(1L, recipeDtoIn1);
        recipeControllerUsers.postRecipe(1L, recipeDtoIn2);
        recipeControllerUsers.postRecipe(2L, recipeDtoIn3);
        recipeDtoOut2.setCreatedAt(recipeControllerUsers.getRecipe(1L, 2L).getCreatedAt());
        assertEquals(recipeDtoOut2, recipeControllerUsers.getRecipe(1L, 2L));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws ResponseStatusException {
                recipeControllerUsers.getRecipe(2L, 2L);
            }
        });
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    void getRecipes() {
        recipeControllerUsers.postRecipe(1L, recipeDtoIn1);
        recipeControllerUsers.postRecipe(1L, recipeDtoIn2);
        recipeControllerUsers.postRecipe(2L, recipeDtoIn3);
        assertEquals(2,
                recipeControllerUsers.getRecipes(1L, List.of(1L, 2L), 0, 10, RecipeSort.DATE).size());
        assertEquals(1,
                recipeControllerUsers.getRecipes(2L, List.of(1L, 2L, 3L), 0, 10, RecipeSort.DATE).size());
        assertEquals(1,
                recipeControllerUsers.getRecipes(2L, List.of(1L, 2L, 3L, 4L), 0, 10, RecipeSort.DATE).size());
        assertEquals(2,
                recipeControllerUsers.getRecipes(1L, List.of(1L, 2L), 0, 10, RecipeSort.TITLE).size());
    }
}
