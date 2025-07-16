package com.mick.chatop.repository;

import com.mick.chatop.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// Cette interface est responsable de la gestion des opérations CRUD
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
}
