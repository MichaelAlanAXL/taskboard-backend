package dev.michael.taskboard_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "card_block_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude="card")
public class CardBlockLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(name = "blocked_at", nullable = false, updatable = false)
    private LocalDateTime blockedAt;

    @Column(name = "unblocked_at")
    private LocalDateTime unblockedAt;

    @Column(name = "reason_block")
    private String reasonBlock;

    @Column(name = "reason_unblock")
    private String reasonUnblock;  
    
    @PrePersist
    protected void onBlock() {
        this.blockedAt = LocalDateTime.now();
    }
    
}
