package ru.s4idm4de.testCategory;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;
import ru.s4idm4de.category.controller.CategoryController;
import ru.s4idm4de.category.model.Category;
import ru.s4idm4de.category.model.RequestCategory;
import ru.s4idm4de.category.model.dto.CategoryDtoIn;
import ru.s4idm4de.category.model.dto.CategoryDtoOut;
import ru.s4idm4de.category.model.dto.ConfirmedCategoriesDto;
import ru.s4idm4de.category.model.dto.RequestCategoryDtoOut;
import ru.s4idm4de.user.UserController;
import ru.s4idm4de.user.UserService;
import ru.s4idm4de.user.model.Gender;
import ru.s4idm4de.user.model.User;
import ru.s4idm4de.user.model.dto.UserDtoIn;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "db.name=test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryTest {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EntityManager em;

    private final UserController userController;

    private final CategoryController categoryController;

    private UserDtoIn userDtoIn1;

    private UserDtoIn userDtoIn2;

    private User user1;

    private CategoryDtoIn categoryDtoIn1;

    private CategoryDtoIn categoryDtoIn2;

    private CategoryDtoIn categoryDtoIn3;

    private CategoryDtoOut categoryDtoOut1;

    private CategoryDtoOut categoryDtoOut2;

    private CategoryDtoOut categoryDtoOut3;

    private Category category1;

    private Category category2;

    private RequestCategory requestCategory2;

    private RequestCategoryDtoOut requestCategoryDtoOut2;

    private RequestCategoryDtoOut requestCategoryDtoOut3;

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
        userController.postUser(userDtoIn1);
        userController.postUser(userDtoIn2);
        categoryDtoIn1 = CategoryDtoIn.builder().name("category1").isPrivate(true).build();
        categoryDtoIn2 = CategoryDtoIn.builder().name("category2").isPrivate(false).build();
        categoryDtoIn3 = CategoryDtoIn.builder().name("category3").isPrivate(false).build();
        categoryDtoOut1 = CategoryDtoOut.builder().name("category1").isPrivate(true).build();
        categoryDtoOut2 = CategoryDtoOut.builder().name("category2").isPrivate(false).build();
        categoryDtoOut3 = CategoryDtoOut.builder().name("category3").isPrivate(false).build();
        category1 = Category.builder().id(1L).isPrivate(true).name("category1").author(user1).build();
        category2 = Category.builder().id(2L).isPrivate(false).name("category2").author(user1).build();
        requestCategory2 = RequestCategory.builder().createdAt(LocalDateTime.now())
                .id(1L)
                .name("category2")
                .initiator(user1).build();
        requestCategoryDtoOut2 = RequestCategoryDtoOut.builder().initiatorId(1L)
                .createdAt(LocalDateTime.now().format(formatter))
                .id(1L)
                .name("category2")
                .build();
        requestCategoryDtoOut3 = RequestCategoryDtoOut.builder().initiatorId(2L)
                .createdAt(LocalDateTime.now().format(formatter))
                .id(2L)
                .name("category3")
                .build();
    }

    @Test
    void postCategory() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws ResponseStatusException {
                categoryController.postCategory(999L, categoryDtoIn1);
            }
        });
        assertEquals("404 NOT_FOUND \"there is no user with id 999\"; nested exception " +
                "is ru.s4idm4de.exception.NotFoundException: there is no user with id 999", exception.getMessage());
        assertEquals(categoryDtoOut1 , categoryController.postCategory(1L, categoryDtoIn1).getBody());
        assertEquals(categoryDtoOut2, categoryController.postCategory(1L, categoryDtoIn2).getBody());
        TypedQuery<Category> query = em.createQuery("Select c from Category c", Category.class);
        List<Category> categories = query.getResultList();
        assertArrayEquals(new List[]{List.of(category1)},new List[]{categories});
    }

    @Test
    void getRequestCategories() {
        categoryController.postCategory(1L, categoryDtoIn1);
        categoryController.postCategory(1L, categoryDtoIn2);
        categoryController.postCategory(2L, categoryDtoIn3);
        assertArrayEquals(new List[]{List.of(requestCategoryDtoOut3, requestCategoryDtoOut2)},
               new List[]{categoryController.getRequestCategories(1L, 0, 10, null, null).getBody()});
    }

    @Test
    void confirmCategories() {
        ConfirmedCategoriesDto confirmedCategoriesDto = ConfirmedCategoriesDto.builder()
                .confirmedCategories(new HashMap<>(){{
                    put("category1", 1L);
                    put("category2", 1L);
                }}).build();
        categoryController.confirmCategories(1L, confirmedCategoriesDto);
        category1.setIsPrivate(false);
        category1.setId(2L);
        category2.setId(1L);
        TypedQuery<Category> query = em.createQuery("Select c from Category c", Category.class);
        List<Category> categories = query.getResultList();
        assertArrayEquals(new List[]{List.of(category2, category1)}, new List[]{categories});
    }

    @Test
    void getCategory() {
        categoryController.postCategory(1L, categoryDtoIn1);
        assertEquals(categoryDtoOut1, categoryController.getCategory(1L, 1L).getBody());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws ResponseStatusException {
                categoryController.getCategory(2L, 1L);
            }
        });
        assertEquals("409 CONFLICT \"user 2 is not owner of category 1\"; nested exception " +
                "is ru.s4idm4de.exception.ContradictionException: user 2 is not owner of category 1", exception.getMessage());
    }

    @Test
    void getCategories() {
        categoryDtoIn2.setIsPrivate(true);
        categoryController.postCategory(1L, categoryDtoIn1);
        categoryController.postCategory(1L, categoryDtoIn2);
        ConfirmedCategoriesDto confirmedCategoriesDto = ConfirmedCategoriesDto.builder()
                .confirmedCategories(new HashMap<>(){{
                    put("category3", 1L);
                }}).build();
        categoryController.confirmCategories(1L, confirmedCategoriesDto);
        categoryDtoOut2.setIsPrivate(true);
        assertArrayEquals(new List[]{List.of(categoryDtoOut1, categoryDtoOut2)},
                new List[]{categoryController.getCategories(1L, true, 0, 10).getBody()});
        assertArrayEquals(new List[]{List.of(categoryDtoOut3)},
                new List[]{categoryController.getCategories(1L, false, 0, 10).getBody()});
    }
}
