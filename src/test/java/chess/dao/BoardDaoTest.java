package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

import chess.domain.ChessGame;
import chess.domain.board.BoardInitializer;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dto.PieceDto;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardDaoTest {
    @Autowired
    BoardDao boardDao;
    @Autowired
    GameDao gameDao;

    int id;

    @BeforeEach
    void insertGameData() {
        id = gameDao.save(new ChessGame("test", "test"));
        gameDao.updateTurnById(id);
    }

    @DisplayName("DB에 보드를 저장한다.")
    @Test
    void saveTo() {
        Map<Position, Piece> squares = BoardInitializer.get().getSquares();

        assertThatNoException().isThrownBy(() -> boardDao.saveAll(id, squares));
    }

    @DisplayName("DB에 초기 보드를 저장한 후 load하면 a1 위치에 흰색 룩이 있다.")
    @Test
    void load_a1_white_rook() {
        Map<Position, Piece> squares = BoardInitializer.get().getSquares();

        boardDao.saveAll(id, squares);
        List<PieceDto> board = boardDao.findAllByGameId(id);

        PieceDto pieceAtA1 = board.stream()
                .filter(pieceDto -> pieceDto.getPosition().equals("a1"))
                .findAny().get();
        assertThat(pieceAtA1.getCamp() + pieceAtA1.getType()).isEqualTo("whiterook");
    }
}
