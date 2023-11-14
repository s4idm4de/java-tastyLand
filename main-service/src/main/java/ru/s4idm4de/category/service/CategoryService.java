package ru.s4idm4de.category.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.s4idm4de.category.model.Category;
import ru.s4idm4de.category.model.CategoryParams;
import ru.s4idm4de.category.model.QRequestCategory;
import ru.s4idm4de.category.model.dto.CategoryDtoIn;
import ru.s4idm4de.category.model.dto.CategoryDtoOut;
import ru.s4idm4de.category.model.dto.ConfirmedCategoriesDto;
import ru.s4idm4de.category.model.dto.RequestCategoryDtoOut;
import ru.s4idm4de.category.model.mapper.CategoryMapper;
import ru.s4idm4de.category.repository.CategoryRepository;
import ru.s4idm4de.category.repository.RequestCategoryRepository;
import ru.s4idm4de.exception.ContradictionException;
import ru.s4idm4de.exception.NotFoundException;
import ru.s4idm4de.user.UserRepository;
import ru.s4idm4de.user.model.User;


import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final RequestCategoryRepository requestCategoryRepository;

    @Autowired
    private final UserRepository userRepository;

    public CategoryDtoOut postCategory(Long userId, CategoryDtoIn categoryDtoIn) {
        try {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NotFoundException("there is no user with id " + userId)
            );
            Category category = CategoryMapper.toCategory(categoryDtoIn);
            category.setAuthor(user);
            if (categoryDtoIn.getIsPrivate().equals(true)) {
                return CategoryMapper.toCategoryDtoOut(categoryRepository.save(category));
            } else {
                requestCategoryRepository.save(CategoryMapper.toRequestCategory(categoryDtoIn, user));
                return CategoryMapper.toCategoryDtoOut(category);
            }
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public List<CategoryDtoOut> getCategories(Long userId, CategoryParams categoryParams) {
        if (!categoryParams.getIsPrivate()) {
            return CategoryMapper.toCategoryDtoOut(categoryRepository.findAllByIsPrivate(
                    false,
                    PageRequest.of(categoryParams.getFrom() / categoryParams.getSize(),
                            categoryParams.getSize(),
                            Sort.by("name"))));
        } else {
            return CategoryMapper.toCategoryDtoOut(categoryRepository.findAllByIsPrivateAndAuthor_Id(
                    true, userId,
                    PageRequest.of(categoryParams.getFrom() / categoryParams.getSize(),
                            categoryParams.getSize(),
                            Sort.by("name"))));
        }
    }

    public CategoryDtoOut getCategory(Long userId, Long categoryId) {
        try {
            User author = userRepository.findById(userId).orElseThrow(
                    () -> new NotFoundException("no user with Id " + userId)
            );
            Category category = categoryRepository.findById(categoryId).orElseThrow(
                    () -> new NotFoundException("no category with Id " + categoryId)
            );
            if (!category.getIsPrivate() || category.getAuthor().equals(author)) {
                return CategoryMapper.toCategoryDtoOut(category);
            } else {
                throw new ContradictionException("user " + userId + " is not owner of category " + categoryId);
            }
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ContradictionException e) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    public List<RequestCategoryDtoOut> getRequestCategories(CategoryParams categoryParams) {
        BooleanExpression expression = QRequestCategory.requestCategory.id.gt(0L);
        if(categoryParams.getStartDate() != null) {
            expression = expression.and(QRequestCategory.requestCategory.createdAt.after(categoryParams.getStartDate()));
        }
        if(categoryParams.getEndDate() != null) {
            expression = expression.and(QRequestCategory.requestCategory.createdAt.before(categoryParams.getEndDate()));
        }
        return CategoryMapper.toRequestCategoryDtoOut(requestCategoryRepository.findAll(expression,
                PageRequest.of(categoryParams.getFrom()/categoryParams.getSize(), categoryParams.getSize(),
                        Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

    public void confirmCategories(Long adminId, ConfirmedCategoriesDto confirmedCategoriesDto) {
        confirmedCategoriesDto.getConfirmedCategories().forEach((name, authorId) -> {
            try {
                User author = userRepository.findById(authorId).orElseThrow(() -> new NotFoundException("no user " + authorId));
                Category category = Category.builder()
                        .name(name)
                        .author(author)
                        .isPrivate(false).build();
                categoryRepository.save(category);
            } catch (NotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
        });
    }
}
