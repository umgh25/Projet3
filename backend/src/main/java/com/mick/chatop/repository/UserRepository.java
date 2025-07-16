package com.mick.chatop.repository;

import com.mick.chatop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Cette interface est responsable de la gestion des opérations CRUD
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);
}
