package ru.s4idm4de.recipe.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s4idm4de.category.model.Category;
import ru.s4idm4de.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "recipes", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @Transient
    private Long likes;

    @NotNull
    @ManyToMany
    @JoinTable(
            name = "recipes_categories",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private final Set<Category> categories = new HashSet<>();

    @NotBlank
    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "video")
    private String video;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany
    @JoinColumn(name = "recipe_id")
    private final Set<Image> images = new HashSet<>();

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void addImage(Image image) {
        images.add(image);
    }
}
