package ru.s4idm4de.testUser;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;
import ru.s4idm4de.exception.RequestExceptionHandler;
import ru.s4idm4de.user.UserController;
import ru.s4idm4de.user.UserService;
import ru.s4idm4de.user.model.Gender;
import ru.s4idm4de.user.model.User;
import ru.s4idm4de.user.model.dto.UserDtoIn;
import ru.s4idm4de.user.model.dto.UserDtoOut;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "db.name=test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest {
    private final EntityManager em;

    private final UserService userService;

    private final UserController userController;

    private User user1;

    private User user2;

    private UserDtoIn userDtoIn1;

    private UserDtoIn userDtoIn2;

    private UserDtoOut userDtoOut1;

    private UserDtoOut userDtoOut2;

    @BeforeEach
    void beforeEach() {
        user1 = User.builder().id(1L)
                .email("user1@mail.ru")
                .name("User1")
                .gender(Gender.FEMALE)
                .birthday(LocalDate.of(1998, 7, 2))
                .allowToPublish(true).build();
        user2 = User.builder().id(2L)
                .email("user2@mail.ru")
                .name("User2")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1998, 7, 2))
                .allowToPublish(true).build();
        userDtoIn1 = UserDtoIn.builder().name("User1")
                .birthday(LocalDate.of(1998, 7, 2))
                .gender(Gender.FEMALE)
                .email("user1@mail.ru").build();
        userDtoIn2 = UserDtoIn.builder()
                .email("user2@mail.ru")
                .name("User2")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1998, 7, 2))
                .build();
        userDtoOut1 = UserDtoOut.builder()
                .email("user1@mail.ru")
                .name("User1")
                .gender(Gender.FEMALE)
                .age(25)
                .build();

        userDtoOut2 = UserDtoOut.builder()
                .email("user2@mail.ru")
                .name("User2")
                .gender(Gender.FEMALE)
                .age(25)
                .build();

    }

    @Test
    void postUserTest() {
        userController.postUser(userDtoIn1);
        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", 1L)
                .getSingleResult();
        assertThat(user, equalTo(user1));
    }

    @Test
    void patchUserTest() {
        userController.postUser(userDtoIn1);
        UserDtoIn userDtoIn1New = userDtoIn1.toBuilder().name("updated").build();
        userController.patchUser(userDtoIn1New, 1L);
        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", 1L)
                .getSingleResult();
        user1.setName("updated");
        assertThat(user, equalTo(user1));
    }

    @Test
    void getUserTest() {
        userController.postUser(userDtoIn1);
        assertEquals(userDtoOut1, userController.getUser(1L).getBody());
    }

    @Test
    void deleteUserTest() {
        userController.postUser(userDtoIn1);
        userController.postUser(userDtoIn2);
        userController.deleteUser(1L);
        TypedQuery<User> query = em.createQuery("Select u from User u", User.class);
        List<User> users = query.getResultList();
        assertArrayEquals(new List[]{users}, new List[]{List.of(user2)});
    }
}
