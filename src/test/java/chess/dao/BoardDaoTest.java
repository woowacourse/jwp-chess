package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.ChessGame;
import chess.domain.board.BoardInitializer;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dto.PieceDto;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
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
    Long gameId;

    @BeforeEach
    void setUp() {
        gameId = gameDao.save(new ChessGame("test", "test"));
        Map<Position, Piece> squares = BoardInitializer.get().getSquares();
        boardDao.saveAll(gameId, squares);
    }

    @AfterEach
    void after() {
        boardDao.deleteAllByGameId(gameId);
        gameDao.deleteById(gameId);
    }

    @DisplayName("새로운 게임 보드가 저장되었는지 테스트한다.")
    @Test
    void findAllByGameId() {
        List<PieceDto> pieces = boardDao.findAllByGameId(gameId);

        assertThat(pieces.size()).isEqualTo(64);
    }

    @DisplayName("DB에 초기 보드를 저장한 후 load하면 a1 위치에 흰색 룩이 있다.")
    @Test
    void save_a1_white_rook() {
        List<PieceDto> board = boardDao.findAllByGameId(gameId);

        PieceDto pieceAtA1 = board.stream()
                .filter(pieceDto -> pieceDto.getPosition().equals("a1"))
                .findAny().get();
        assertThat(pieceAtA1.getCamp() + pieceAtA1.getType()).isEqualTo("whiterook");
    }
}
