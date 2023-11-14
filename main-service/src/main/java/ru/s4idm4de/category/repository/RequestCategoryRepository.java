package ru.s4idm4de.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.s4idm4de.category.model.RequestCategory;

public interface RequestCategoryRepository extends JpaRepository<RequestCategory, Long>, QuerydslPredicateExecutor<RequestCategory> {
}
