package ru.s4idm4de.category.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryParams {

    private Boolean isPrivate;

    private Integer size;

    private Integer from;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long userId;
}
