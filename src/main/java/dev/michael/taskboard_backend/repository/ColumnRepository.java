package dev.michael.taskboard_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.michael.taskboard_backend.entity.ColumnEntity;

public interface ColumnRepository extends JpaRepository<ColumnEntity, Long> {
    
}
