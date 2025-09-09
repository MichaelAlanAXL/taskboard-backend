package dev.michael.taskboard_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.michael.taskboard_backend.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT c FROM Card c " +
            "JOIN FETCH c.column col " +
            "JOIN FETCH col.board b " +
            "JOIN FETCH b.columns " + 
            "WHERE c.id = :id")
    Optional<Card> findByIdWithColumnAndBoard(@Param("id") Long id);
}
