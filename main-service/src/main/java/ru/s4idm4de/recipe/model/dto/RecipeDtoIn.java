package ru.s4idm4de.recipe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDtoIn {

    @NotBlank
    private String title;

    private String text;

    private String video;

    @NotNull
    private List<Long> categoryIds;

    private Boolean isPrivate;

    private List<String> images;
}
