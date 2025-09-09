package dev.michael.taskboard_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.michael.taskboard_backend.entity.Card;
import dev.michael.taskboard_backend.entity.ColumnEntity;
import dev.michael.taskboard_backend.entity.ColumnType;
import dev.michael.taskboard_backend.repository.CardRepository;
import jakarta.transaction.Transactional;

@Service
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
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

        card.setColumn(cancelColumn);
        card.setCanceledAt(LocalDateTime.now());
        // pegar usuário atual 
        card.setCanceledBy(System.getProperty("user.name"));

        cardRepository.save(card);
    }

    @Transactional
    public void moveCard(Long cardId, ColumnEntity destColumn) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new RuntimeException("Card não encontrado: " + cardId));
        card.setColumn(destColumn);
        cardRepository.save(card);
    }
    
}
