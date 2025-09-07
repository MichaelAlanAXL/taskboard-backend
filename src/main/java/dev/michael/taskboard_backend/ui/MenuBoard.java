package dev.michael.taskboard_backend.ui;

import java.util.Scanner;

import dev.michael.taskboard_backend.entity.Board;
import dev.michael.taskboard_backend.entity.ColumnEntity;
import dev.michael.taskboard_backend.entity.ColumnType;
import dev.michael.taskboard_backend.service.CardService;

public class MenuBoard {
    private final Board board;
    private final Scanner scanner;
    private final CardService cardService;

    public MenuBoard(Board board, CardService cardService, Scanner scanner) {
        this.board = board;
        this.scanner = scanner;
        this.cardService = cardService;
    }

    public void showMenu() {
        int option = -1;

        while (option != 6) {
            System.out.println("\n===== MENU DO BOARD: " + board.getName() + " =====");
            System.out.println("1 - Criar card");
            System.out.println("2 - Mover card");
            System.out.println("3 - Cancelar card");
            System.out.println("4 - Bloquear card");
            System.out.println("5 - Desbloquear card");
            System.out.println("6 - Fechar board");
            System.out.print("Escolha uma opção: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> createCard();
                case 2 -> moveCard();
                case 3 -> System.out.println("Cancelar card (em breve)");
                case 4 -> System.out.println("Bloquear card (em breve)");
                case 5 -> System.out.println("Desbloquear card (em breve)");
                case 6 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("Opção inválida!");

            }
        }
    }

    // Criar novo card
    private void createCard() {
        System.out.println("\n--- Criar Card ---");
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Descrição: ");
        String description = scanner.nextLine();

        ColumnEntity column = board.getColumns().stream()
            .filter(c -> c.getType() == ColumnType.INITIAL)
            .findFirst()
            .orElseThrow(() -> new RuntimeException(" Coluna inicial não encontrada!"));

        cardService.createCard(title, description, (ColumnEntity) column);
        System.out.println("Card criado na coluna inicial: " + column.getName());
    }

    // mover status do card
    private void moveCard() {
        System.out.println("\n--- Mover Card ---");

    // Lista os cards do board
    if (board.getColumns().isEmpty()) {        
        System.out.println("Não há colunas no board.");
        return;
    }

    boolean temCards = board.getColumns().stream()
        .anyMatch(c -> !c.getCards().isEmpty());

    if (!temCards) {
        System.out.println("Não há cards para mover.");
        return;
    }

    System.out.println("Cards disponíveis:");
    board.getColumns().forEach(col -> {
        col.getCards().forEach(card -> {
            System.out.println(card.getId() + " - " + card.getTitle() + " (Coluna: " + col.getName() + ")");
        });
    });

    System.out.print("Digite o ID do card que deseja mover: ");
    Long cardId = scanner.nextLong();
    scanner.nextLine();

    // Buscar o card pelo ID
    var card = cardService.findCardById(cardId);
    if (card == null) {
        System.out.println("Card não encontrado!");
        return;
    }

    // Lista as colunas do board
    System.out.println("\nColunas disponíveis:");
    board.getColumns().forEach(c -> {
        System.out.println(c.getId() + " - " + c.getName());
    });

    System.out.print("Digite o ID da coluna destino: ");
    Long columnId = scanner.nextLong();
    scanner.nextLine();

    var colunaDestino = board.getColumns().stream()
        .filter(c -> c.getId().equals(columnId))
        .findFirst()
        .orElse(null);

    if (colunaDestino == null) {
        System.out.println("Coluna destino inválida!");
        return;
    }

    cardService.moveCard(card, colunaDestino);
    System.out.println("Card movido para a coluna: " + colunaDestino.getName());
    }

    // private void cancelCard() {
    //     System.out.println("Cancelar card (em breve)");
    // }

    // private void blockCard() {
    //     System.out.println("Bloquear card (em breve)");
    // }

    // private void unblockCard() {
    //     System.out.println("Desbloquear card (em breve)");
    // }
}
