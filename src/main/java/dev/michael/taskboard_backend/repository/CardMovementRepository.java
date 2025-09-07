package dev.michael.taskboard_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.michael.taskboard_backend.entity.CardMovement;

public interface CardMovementRepository extends JpaRepository<CardMovement, Long> {
    
}
