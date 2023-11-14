package ru.s4idm4de.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.s4idm4de.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
