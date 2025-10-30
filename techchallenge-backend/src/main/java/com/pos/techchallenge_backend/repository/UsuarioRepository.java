package com.pos.techchallenge_backend.repository;

import com.pos.techchallenge_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    Optional<Usuario> findByLoginAndSenha(String login, String senha);
}
