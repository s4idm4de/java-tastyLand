package ru.s4idm4de.category.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s4idm4de.user.model.User;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "categories", schema = "public",
        uniqueConstraints = { @UniqueConstraint(name = "name_private_constraint",
                columnNames = { "name", "is_private" }) })
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

}
