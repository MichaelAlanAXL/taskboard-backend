package dev.michael.taskboard_backend.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.michael.taskboard_backend.entity.Board;
import dev.michael.taskboard_backend.entity.ColumnEntity;
import dev.michael.taskboard_backend.entity.ColumnType;
import dev.michael.taskboard_backend.repository.BoardRepository;
import dev.michael.taskboard_backend.repository.ColumnRepository;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;

    public BoardService(BoardRepository boardRepository, ColumnRepository columnRepository) {
        this.boardRepository = boardRepository;
        this.columnRepository = columnRepository;
    }

    public Board createBoardIfNotExists(String name) {
        Board existing = boardRepository.findByName(name);
        if (existing != null) {
            return existing;
        }
        Board board = new Board();
        board.setName(name);
        board = boardRepository.save(board);

        if (board.getColumns().isEmpty()) {
            // Cria colunas padrão
            ColumnEntity colInicial = new ColumnEntity(board, "Backlog", ColumnType.INITIAL, 1);
            ColumnEntity colPendente = new ColumnEntity(board, "In Progress", ColumnType.PENDING, 2);
            ColumnEntity colFinal = new ColumnEntity(board, "Done", ColumnType.FINAL, 3);
            ColumnEntity colCancelamento = new ColumnEntity(board, "Canceled", ColumnType.CANCELED, 4);
    
            columnRepository.saveAll(Arrays.asList(colInicial, colPendente, colFinal, colCancelamento));

        }
                
        return board;
    }

    public Board findBoardByName(String name) {
        return boardRepository.findByName(name);
    }

    public List<Board> listBoards() {
        return boardRepository.findAll();
    }

    public Board findBoardById(Long id) {
        return boardRepository.findByIdWithColumns(id);
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
    
}
