package dev.michael.taskboard_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.michael.taskboard_backend.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
    
}
