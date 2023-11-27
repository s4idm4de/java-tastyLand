package ru.s4idm4de.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s4idm4de.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "like_recipes", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "user_recipe_constraint",
        columnNames = {"user_id", "recipe_id"})})
@AllArgsConstructor
@NoArgsConstructor
public class LikeRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
