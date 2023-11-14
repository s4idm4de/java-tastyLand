package ru.s4idm4de.user.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "users", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 250, message = "your name is too long")
    @Column(name = "name", length = 250, unique = true)
    private String name;

    @Email
    @Column(name = "email", length = 250, unique = true)
    private String email;

    @Size(max = 250, message = "your status is too long")
    @Column(name = "status", length = 250)
    private String status;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "allow_to_publish")
    private Boolean allowToPublish = true;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
