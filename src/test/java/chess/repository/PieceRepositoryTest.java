package chess.repository;

import static org.assertj.core.api.Assertions.*;

import chess.dao.FakePieceDao;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.dto.BoardElementDto;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PieceRepositoryTest {
    private PieceRepository pieceRepository;

    @BeforeEach
    void setUp() {
        pieceRepository = new PieceRepository(new FakePieceDao());
        pieceRepository.saveAllPieceToStorage(1, new BoardInitializer().init());
    }

    @AfterEach
    void clean() {
        pieceRepository.deleteAllPiece(1);
    }

    @Test
    void findAllTest() {
        List<BoardElementDto> boardElementDtos = pieceRepository.findAll(1);
        assertThat(boardElementDtos.size()).isEqualTo(32);
    }

    @Test
    void updatePiecePositionTest() {
        pieceRepository.updatePiecePosition(1, "A2", "A3");
        List<BoardElementDto> boardElementDtos = pieceRepository.findAll(1);
        int condition = 0;
        for (BoardElementDto boardElementDto : boardElementDtos) {
            if (boardElementDto.getPosition().equals("A3")) {
                ++condition;
            }
        }
        assertThat(condition).isEqualTo(1);
    }

    @Test
    void getBoardFromStorageTest() {
        Board board = pieceRepository.getBoardFromStorage(1);
        Board initBoard = new Board(new BoardInitializer());
        assertThat(board).isEqualTo(initBoard);
    }

    @Test
    void deletePieceTest() {
        pieceRepository.deletePiece(1, "A2");
        List<BoardElementDto> boardElementDtos = pieceRepository.findAll(1);
        int condition = 0;
        for (BoardElementDto boardElementDto : boardElementDtos) {
            if (boardElementDto.getPosition() == "A2") {
                ++condition;
            }
        }
        assertThat(boardElementDtos.size() == 31 && condition == 0).isTrue();
    }

}
