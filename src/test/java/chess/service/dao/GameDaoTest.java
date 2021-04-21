package chess.service.dao;

import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.board.position.Position;
import chess.domain.player.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class GameDaoTest {

    @Autowired GameDao gameDao;
    private Long roomId;
    private Turn turn;
    private Board board;


    @BeforeEach
    void beforeEach() {
        roomId = 123L;
        turn = Turn.WHITE;
        board = BoardInitializer.initiateBoard();
    }

    @Test
    void testLoad() {
        gameDao.save(roomId, turn, board);

        assertThat(gameDao.load(roomId).get().getTurn()).isEqualTo("WHITE");
    }

    @Test
    void testUpdate() {
        gameDao.save(roomId, turn, board);

        board.movePiece(new Position("a2"), new Position("a4")); // white pawn 이동
        board.movePiece(new Position("a1"), new Position("a3")); // white rook 이동

        gameDao.update(roomId, Turn.BLACK, board);

        String[] boardArray = gameDao.load(roomId).get().getBoard().split(",");
        assertThat(boardArray[2]).isEqualTo("&#9814;"); // a3 - rook
    }

    @Test
    void testDelete() {
        gameDao.save(roomId, turn, board);
        gameDao.delete(roomId);

        assertThat(gameDao.load(roomId).equals(Optional.empty())).isTrue();
    }
}