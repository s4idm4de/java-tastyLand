package ru.s4idm4de.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.s4idm4de.recipe.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
