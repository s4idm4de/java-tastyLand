package ru.s4idm4de.category.model.mapper;

import ru.s4idm4de.category.model.Category;
import ru.s4idm4de.category.model.RequestCategory;
import ru.s4idm4de.category.model.dto.CategoryDtoIn;
import ru.s4idm4de.category.model.dto.CategoryDtoOut;
import ru.s4idm4de.category.model.dto.RequestCategoryDtoOut;
import ru.s4idm4de.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Category toCategory(CategoryDtoIn categoryDtoIn) {
        return Category.builder()
                .name(categoryDtoIn.getName())
                .isPrivate(categoryDtoIn.getIsPrivate())
                .build();
    }

    public static CategoryDtoOut toCategoryDtoOut(Category category) {
        return CategoryDtoOut.builder()
                .name(category.getName())
                .isPrivate(category.getIsPrivate())
                .build();
    }

    public static List<CategoryDtoOut> toCategoryDtoOut(Iterable<Category> categories) {
        List<CategoryDtoOut> result = new ArrayList<>();
        for (Category category : categories) {
            result.add(toCategoryDtoOut(category));
        }
        return result;
    }

    public static RequestCategory toRequestCategory(CategoryDtoIn categoryDtoIn, User user) {
        return RequestCategory.builder()
                .name(categoryDtoIn.getName())
                .initiator(user)
                .createdAt(LocalDateTime.now()).build();
    }

    public static RequestCategoryDtoOut toRequestCategoryDtoOut(RequestCategory requestCategory) {
        return RequestCategoryDtoOut.builder()
                .id(requestCategory.getId())
                .name(requestCategory.getName())
                .createdAt(requestCategory.getCreatedAt().format(formatter))
                .initiatorId(requestCategory.getInitiator().getId()).build();
    }

    public static List<RequestCategoryDtoOut> toRequestCategoryDtoOut(Iterable<RequestCategory> requestCategories) {
        List<RequestCategoryDtoOut> result = new ArrayList<>();
        for (RequestCategory requestCategory : requestCategories) {
            result.add(toRequestCategoryDtoOut(requestCategory));
        }
        return result;
    }
}
