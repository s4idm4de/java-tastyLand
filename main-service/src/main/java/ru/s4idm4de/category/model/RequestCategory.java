package ru.s4idm4de.category.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s4idm4de.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "request_categories", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class RequestCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
