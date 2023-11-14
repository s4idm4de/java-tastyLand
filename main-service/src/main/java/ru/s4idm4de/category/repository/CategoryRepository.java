package ru.s4idm4de.category.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.s4idm4de.category.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, QuerydslPredicateExecutor<Category> {

    List<Category> findAllByIsPrivate(Boolean isPrivate, PageRequest pageRequest);

    List<Category> findAllByIsPrivateAndAuthor_Id(Boolean isPrivate, Long authorId, PageRequest pageRequest);
}
