package ru.s4idm4de.user;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.s4idm4de.exception.NotFoundException;
import ru.s4idm4de.user.model.User;
import ru.s4idm4de.user.model.dto.UserDtoIn;
import ru.s4idm4de.user.model.dto.UserDtoOut;
import ru.s4idm4de.user.model.mapper.UserMapper;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserDtoOut postUser(UserDtoIn userDtoIn) {
        User user = UserMapper.toUser(userDtoIn);
        return UserMapper.toUserDtoOut(userRepository.save(user));
    }

    public UserDtoOut patchUser(UserDtoIn userDtoIn, Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("no user with id "
                    + userId));
            @Valid User validUser = UserMapper.toValidUser(userDtoIn, user);
            return UserMapper.toUserDtoOut(userRepository.save(validUser));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public UserDtoOut getUser(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() ->
                    new NotFoundException("no user with id" + userId));
            return UserMapper.toUserDtoOut(userRepository.save(user));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public void deleteUser(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("no user with id "
                    + userId));
            userRepository.delete(user);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
