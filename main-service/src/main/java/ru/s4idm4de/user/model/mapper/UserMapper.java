package ru.s4idm4de.user.model.mapper;

import javax.validation.Valid;
import ru.s4idm4de.user.model.User;
import ru.s4idm4de.user.model.dto.UserDtoIn;
import ru.s4idm4de.user.model.dto.UserDtoOut;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User toUser(UserDtoIn userDtoIn) {
        return User.builder()
                .birthday(userDtoIn.getBirthday())
                .email(userDtoIn.getEmail())
                .name(userDtoIn.getName())
                .gender(userDtoIn.getGender())
                .allowToPublish(true)
                .build();
    }

    public static User toValidUser(UserDtoIn userDtoIn, User user) {
        @Valid User validUser = User.builder()
                .id(user.getId())
                .gender(userDtoIn.getGender() == null? user.getGender() : userDtoIn.getGender())
                .name(userDtoIn.getName() == null? user.getName() : userDtoIn.getName())
                .email(userDtoIn.getEmail() == null? user.getEmail() : userDtoIn.getEmail())
                .birthday(userDtoIn.getBirthday() == null? user.getBirthday() : userDtoIn.getBirthday())
                .allowToPublish(user.getAllowToPublish())
                .status(userDtoIn.getStatus() == null? user.getStatus() : userDtoIn.getStatus())
                .build();
        return validUser;
    }

    public static UserDtoOut toUserDtoOut(User user) {
        return UserDtoOut.builder()
                .age(Period.between(user.getBirthday(), LocalDate.now()).getYears())
                .email(user.getEmail())
                .gender(user.getGender())
                .name(user.getName())
                .status(user.getStatus()).build();
    }

    public static List<UserDtoOut> toUserDtoOut(Iterable<User> users) {
        List<UserDtoOut> result = new ArrayList<>();
        for(User user : users) {
            result.add(toUserDtoOut(user));
        }
        return result;
    }
}
