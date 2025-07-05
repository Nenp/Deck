package tech.sanak.deck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.sanak.deck.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
