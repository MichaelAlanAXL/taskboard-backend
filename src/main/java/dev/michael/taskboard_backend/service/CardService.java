package dev.michael.taskboard_backend.service;

import org.springframework.stereotype.Service;

import dev.michael.taskboard_backend.entity.Card;
import dev.michael.taskboard_backend.entity.ColumnEntity;
import dev.michael.taskboard_backend.repository.CardRepository;

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

    public Card findCardById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    public void moveCard(Card card, ColumnEntity newColumn) {
        card.setColumn(newColumn);
        cardRepository.save(card);
    }
    
}
