package ru.s4idm4de.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s4idm4de.user.model.Gender;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoIn {
    @NotBlank
    @Size(max = 250, message = "your name too long")
    private String name;

    @Email
    private String email;

    @Size(max = 250, message = "describe yourself in fewer words")
    private String status;

    @Past
    private LocalDate birthday;

    @NotNull
    private Gender gender;
}
