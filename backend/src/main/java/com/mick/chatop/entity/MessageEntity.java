package com.mick.chatop.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// Cette classe représente une entité de message dans l'application.
@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 2000)
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "rental_id", referencedColumnName = "id", nullable = false)
    private RentalEntity rental;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;

    public MessageEntity() {
        // Constructeur par défaut requis par JPA
    }

    public MessageEntity(RentalEntity rental, UserEntity user, String message) {
        this.rental = rental;
        this.user = user;
        this.message = message;
    }

    // Getters et setters

    public Integer getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public RentalEntity getRental() {
        return rental;
    }

    public void setRental(RentalEntity rental) {
        this.rental = rental;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}