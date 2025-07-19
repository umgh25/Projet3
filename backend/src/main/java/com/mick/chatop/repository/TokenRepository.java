package com.mick.chatop.repository;

import com.mick.chatop.entity.TokenEntity;
import com.mick.chatop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * Repository pour gérer les opérations CRUD sur les tokens JWT.
 * Permet de trouver des tokens par leur valeur et de récupérer tous les tokens valides d'un utilisateur.
 */
@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByToken(String token);
    List<TokenEntity> findAllByUserAndValidTrue(UserEntity user);
} 