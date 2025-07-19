package com.mick.chatop.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Entité représentant un token JWT stocké dans la base de données.
 * Contient des informations sur le token, l'utilisateur associé, et sa validité.
 */
@Entity
@Table(name = "tokens")
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private boolean valid = true;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
} 