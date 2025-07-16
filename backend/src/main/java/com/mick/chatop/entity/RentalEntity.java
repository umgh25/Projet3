package com.mick.chatop.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// Cette classe représente une entité de location dans l'application.
@Entity
@Table(name="rentals")
public class RentalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double surface;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private String picture; // URL de l'image

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id",nullable = false)
    private UserEntity owner;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;

    public RentalEntity() {
    }

    public RentalEntity(
            Integer id,
            String name,
            Double surface,
            Double price,
            String description,
            String picture,
            UserEntity owner,
            LocalDateTime created_at,
            LocalDateTime updated_at) {
        this.id = id;
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.description = description;
        this.picture = picture;
        this.owner = owner;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public RentalEntity(String name, Double surface, Double price, String description) {
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
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
