package dev.michael.taskboard_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.michael.taskboard_backend.entity.Card;
import dev.michael.taskboard_backend.entity.CardBlockLog;
import dev.michael.taskboard_backend.entity.CardMovement;
import dev.michael.taskboard_backend.entity.ColumnEntity;
import dev.michael.taskboard_backend.entity.ColumnType;
import dev.michael.taskboard_backend.repository.CardBlockLogRepository;
import dev.michael.taskboard_backend.repository.CardMovementRepository;
import dev.michael.taskboard_backend.repository.CardRepository;
import jakarta.transaction.Transactional;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final CardBlockLogRepository cardBlockLogRepository;
    private final CardMovementRepository cardMovementRepository;

    public CardService(CardRepository cardRepository, CardBlockLogRepository cardBlockLogRepository, CardMovementRepository cardMovementRepository) {
        this.cardRepository = cardRepository;
        this.cardBlockLogRepository = cardBlockLogRepository;
        this.cardMovementRepository = cardMovementRepository;
    }

    public Card createCard(String title, String description, ColumnEntity initialColumn) {
        Card card = new Card();
        card.setTitle(title);
        card.setDescription(description);
        card.setColumn(initialColumn);
        return cardRepository.save(card);
    }

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public Card findCardById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Transactional
    public void cancelCard(Long id) {
        Card card = cardRepository.findByIdWithColumnAndBoard(id)
            .orElseThrow(() -> new RuntimeException("Card não encontrado: " + id));            

        ColumnEntity cancelColumn = card.getColumn().getBoard().getColumns().stream()
            .filter(c -> c.getType() == ColumnType.CANCELED)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Coluna de cancelamento não encontrada!"));

        ColumnEntity fromColumn = card.getColumn(); // coluna atual antes de mover
        card.setColumn(cancelColumn);
        card.setCanceledAt(LocalDateTime.now());
        // pegar usuário atual 
        card.setCanceledBy(System.getProperty("user.name"));
        cardRepository.save(card);

        registerMovement(card, fromColumn, cancelColumn); // registra o cancelamento
    }

    @Transactional
    public void moveCard(Long cardId, ColumnEntity destColumn) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new RuntimeException("Card não encontrado: " + cardId));

        ColumnEntity fromColumn = card.getColumn(); // coluna atual antes de mover
        card.setColumn(destColumn);
        cardRepository.save(card);

        registerMovement(card, fromColumn, destColumn); // registra o movimento
    }

    @Transactional
    public void blockCard(Long id, String reason) {
        Card card = cardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Card não encontrado: " + id));

        if (card.isBlocked()) {
            throw new RuntimeException("Card já está bloqueado!");
        }

        ColumnEntity fromColumn = card.getColumn(); // coluna atual antes de bloquear

        card.setBlocked(true);
        card.setBlockedReason(reason);

        CardBlockLog log = new CardBlockLog();
        log.setCard(card);
        log.setReasonBlock(reason);        
        cardBlockLogRepository.save(log);
        cardRepository.save(card);

        registerMovement(card, fromColumn, fromColumn); // não muda coluna, mas registra a ação
    }

    @Transactional
    public void unblockCard(Long id, String reason) {
        Card card = cardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Card não encontrado: " + id));

        if (!card.isBlocked()) {
            throw new RuntimeException("Card não está bloqueado!");
        }

        ColumnEntity fromColumn = card.getColumn(); // coluna atual antes de desbloquear

        card.setBlocked(false);
        card.setBlockedReason(null);

        CardBlockLog log = cardBlockLogRepository.findAll().stream()
            .filter(l -> l.getCard().getId().equals(card.getId()) && l.getUnblockedAt() == null)
            .reduce((first, second) -> second) // pega o último motivo de bloqueio
            .orElseThrow(() -> new RuntimeException("Nenhum log de bloqueio encontrado!"));

        log.setUnblockedAt(LocalDateTime.now());
        log.setReasonUnblock(reason);

        cardBlockLogRepository.save(log);
        cardRepository.save(card);

        registerMovement(card, fromColumn, fromColumn); // nao muda de coluna só registra a ação
    }

    @Transactional
    public void registerMovement(Card card, ColumnEntity fromColumn, ColumnEntity toColumn) {
        CardMovement movement = new CardMovement();
        movement.setCard(card);
        movement.setFromColumn(fromColumn);
        movement.setToColumn(toColumn);
        movement.setEnteredAt(LocalDateTime.now());
        cardMovementRepository.save(movement);

    }
    
}
