package ru.s4idm4de.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s4idm4de.user.model.Gender;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoOut {
    private String name;

    private String email;

    private String status;

    private Integer age;

    private Gender gender;
}
