package dev.michael.taskboard_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.michael.taskboard_backend.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.columns WHERE b.id = :id")
    Board findByIdWithColumns(@Param("id") Long id);
    
    Board findByName(String name);
    
    
}
