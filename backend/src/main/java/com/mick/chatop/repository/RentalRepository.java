package com.mick.chatop.repository;

import com.mick.chatop.entity.RentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// Cette interface est responsable de la gestion des opérations CRUD
public interface RentalRepository extends JpaRepository<RentalEntity, Integer> {
}
