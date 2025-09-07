package dev.michael.taskboard_backend.ui;

import java.util.List;
import java.util.Scanner;

import dev.michael.taskboard_backend.entity.Board;
import dev.michael.taskboard_backend.service.BoardService;
import dev.michael.taskboard_backend.service.CardService;

public class MenuPrincipal {
    private final BoardService boardService;
    private final CardService cardService;
    private final Scanner scanner;

    public MenuPrincipal(BoardService boardService, CardService cardService, Scanner scanner) {
        this.boardService = boardService;
        this.cardService = cardService;
        this.scanner = scanner;
    }

    public void exibir() {
        int opcao = -1;

        while(opcao != 4) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1 - Criar novo board");
            System.out.println("2 - Selecionar board");
            System.out.println("3 - Excluir board");
            System.out.println("4 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> criarBoard();
                case 2 -> selecionarBoard();
                case 3 -> excluirBoards();
                case 4 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void criarBoard() {
        System.out.print("Digite o nome do novo board: ");
        String nome = scanner.nextLine();
        Board board = boardService.createBoardIfNotExists(nome);
        System.out.println("Board criado: " + board.getName());
    }

    private void selecionarBoard() {
        List<Board> boards = boardService.listBoards();
        if (boards.isEmpty()) {
            System.out.println("Nenhum board cadastrado.");
            return;
        }

        System.out.println("\nBoards disponíveis:");
        boards.forEach(b -> System.out.println(b.getId() + " - " + b.getName()));

        System.out.print("Digite o ID do board para selecionar: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Board board = boardService.findBoardById(id);
        if (board == null) {
            System.out.println("Board não encontrado!");
            return;
        }

        MenuBoard menuBoard = new MenuBoard(board, cardService, scanner);
        menuBoard.showMenu();
    }

    private void excluirBoards() {
        List<Board> boards = boardService.listBoards();
        if (boards.isEmpty()) {
            System.out.println("Nenhum board cadastrado.");
            return;
        }

        System.out.println("\nBoards disponíveis:");
        boards.forEach(b -> System.out.println(b.getId() + " - " + b.getName()));

        System.out.print("Digite o ID do board para excluir:");
        Long id = scanner.nextLong();
        scanner.nextLine();

        boardService.deleteBoard(id);
        System.out.println("Board excluído com sucesso!");
    }


}
