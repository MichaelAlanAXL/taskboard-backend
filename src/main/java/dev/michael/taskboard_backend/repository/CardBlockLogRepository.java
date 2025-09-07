package dev.michael.taskboard_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.michael.taskboard_backend.entity.CardBlockLog;

public interface CardBlockLogRepository extends JpaRepository<CardBlockLog, Long> {
    
}
