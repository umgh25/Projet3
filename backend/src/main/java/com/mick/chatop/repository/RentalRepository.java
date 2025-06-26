package com.mick.chatop.repository;

import com.mick.chatop.entity.RentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<RentalEntity, Integer> {
}
