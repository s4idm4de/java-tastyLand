package ru.s4idm4de.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.s4idm4de.user.model.dto.UserDtoIn;
import ru.s4idm4de.user.model.dto.UserDtoOut;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    //временное решение. Удалить после отладки регистрации пользоватлетей
    @PostMapping
    public ResponseEntity<UserDtoOut> postUser(@RequestBody @Validated UserDtoIn userDtoIn) {
        log.info("UserController postUser userDtoIn {}", userDtoIn);
        return new ResponseEntity(userService.postUser(userDtoIn), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{userId}")
    public ResponseEntity<UserDtoOut> patchUser(@RequestBody UserDtoIn userDtoIn, @PathVariable Long userId) {
        log.info("UserController patchUser userDtoIn {}, userId {}", userDtoIn, userId);
        return new ResponseEntity(userService.patchUser(userDtoIn, userId), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        log.info("UserController deleteUser userId{}", userId);
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<UserDtoOut> getUser(@PathVariable Long userId) {
        log.info("UserController getUser userId {}", userId);
        return new ResponseEntity(userService.getUser(userId), HttpStatus.OK);
    }
}
