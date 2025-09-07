package dev.michael.taskboard_backend;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import dev.michael.taskboard_backend.service.BoardService;
import dev.michael.taskboard_backend.service.CardService;
import dev.michael.taskboard_backend.ui.MenuPrincipal;

@SpringBootApplication
public class TaskboardBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskboardBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(BoardService boardService, CardService cardService) {
		return args -> {
			Scanner scanner = new Scanner(System.in);

			boardService.createBoardIfNotExists("Board de teste");

			MenuPrincipal menu = new MenuPrincipal(boardService, cardService, scanner);
			menu.exibir();
		};
	}

}
