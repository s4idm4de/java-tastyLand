package ru.s4idm4de.category.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.s4idm4de.category.model.CategoryParams;
import ru.s4idm4de.category.model.dto.CategoryDtoIn;
import ru.s4idm4de.category.model.dto.CategoryDtoOut;
import ru.s4idm4de.category.model.dto.ConfirmedCategoriesDto;
import ru.s4idm4de.category.model.dto.RequestCategoryDtoOut;
import ru.s4idm4de.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
//@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private final CategoryService categoryService;

    @PostMapping(path = "/users/{userId}/categories")
    public ResponseEntity<CategoryDtoOut> postCategory(@PathVariable Long userId,
                                                       @RequestBody @Validated CategoryDtoIn categoryDtoIn) {
        log.info("CategoryController postCategory userId {}, categoryDtoIn {}", userId, categoryDtoIn);
        if (categoryDtoIn.getIsPrivate().equals(true)) {
            return new ResponseEntity(categoryService.postCategory(userId, categoryDtoIn), HttpStatus.CREATED);
        } else {
            return new ResponseEntity(categoryService.postCategory(userId, categoryDtoIn), HttpStatus.OK);
        }
    }

    @GetMapping(path = "/users/{userId}/categories")
    public ResponseEntity<List<CategoryDtoOut>> getCategories(@PathVariable Long userId,
                                                              @RequestParam(defaultValue = "false", name = "isPrivate") Boolean isPrivate,
                                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        CategoryParams categoryParams = CategoryParams.builder().from(from).size(size).isPrivate(isPrivate).build();
        log.info("CategoryController getCategories userId {}, categoryParams {}", userId, categoryParams);
        return new ResponseEntity(categoryService.getCategories(userId, categoryParams), HttpStatus.OK);
    }

    @GetMapping(path = "/users/{userId}/categories/{categoryId}")
    public ResponseEntity<CategoryDtoOut> getCategory(@PathVariable Long userId,
                                                      @PathVariable Long categoryId) {
        log.info("CategoryController getCategory userId {}, categoryId {}", userId, categoryId);
        return new ResponseEntity(categoryService.getCategory(userId, categoryId), HttpStatus.OK);
    }

    @GetMapping(path = "/admins/{adminId}/categories")
    public ResponseEntity<List<RequestCategoryDtoOut>> getRequestCategories(@PathVariable Long adminId,
                                                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                                            @RequestParam(name = "startDate", required = false) String startDate,
                                                                            @RequestParam(name = "endDate", required = false) String endDate) {
        CategoryParams categoryParams = CategoryParams.builder()
                .userId(adminId)
                .from(from)
                .size(size)
                .startDate(startDate == null? null : LocalDateTime.parse(startDate, formatter))
                .endDate(endDate == null? null : LocalDateTime.parse(endDate, formatter)).build();
        log.info("CategoryController getRequestCategories categoryParams {}", categoryParams);
        return new ResponseEntity(categoryService.getRequestCategories(categoryParams), HttpStatus.OK);
    }

    @PostMapping(path = "/admins/{adminId}/categories")
    public ResponseEntity<Void> confirmCategories(@PathVariable Long adminId,
                                                  @RequestBody ConfirmedCategoriesDto confirmedCategoriesDto) {
        log.info("CategoryController confirmCategories adminId {} confirmedCategoriesDto {}", adminId, confirmedCategoriesDto);
        categoryService.confirmCategories(adminId, confirmedCategoriesDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
