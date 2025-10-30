package com.pos.techchallenge_backend.repository;

import com.pos.techchallenge_backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByNameContainingIgnoreCase(String name);

    Optional<User> findByLoginAndPassword(String login, String password);
}
