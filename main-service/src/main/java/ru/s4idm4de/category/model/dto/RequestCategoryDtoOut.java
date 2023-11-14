package ru.s4idm4de.category.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s4idm4de.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RequestCategoryDtoOut {

    private Long id;

    private String name;

    private Long initiatorId;

    private String createdAt;
}
