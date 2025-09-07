package dev.michael.taskboard_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "card_movements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"card", "fromColumn", "toColumn"})
public class CardMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id", nullable=false)
    private Card card;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_column_id")
    private ColumnEntity fromColumn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_column_id")
    private ColumnEntity toColumn;

    private LocalDateTime enteredAt = LocalDateTime.now();

    private LocalDateTime leftAt;

    
}
