package chess.webdao;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
class SpringChessGameDaoTest {

    @Autowired
    SpringChessGameDao springChessGameDao;

    @BeforeEach
    void setUp() {
        springChessGameDao.deleteChessGame();
    }

    @Test
    void createChessGame() {
        final ChessGame chessGame = springChessGameDao.createChessGame();
        assertThat(chessGame.calculateBlackTeamScore()).isEqualTo(38.0);
        assertThat(chessGame.calculateWhiteTeamScore()).isEqualTo(38.0);
        assertThat(chessGame.isPlaying()).isTrue();
        assertThat(chessGame.isWhiteTeamTurn()).isTrue();
    }

    @Test
    void readChessGame() {
        final ChessGame chessGameCreated = springChessGameDao.createChessGame();
        final Map<Position, Piece> positionPieceCreated = chessGameCreated.generateChessBoard();

        final ChessGame chessGameLoaded = springChessGameDao.readChessGame();
        final Map<Position, Piece> positionPieceLoaded = chessGameLoaded.generateChessBoard();

        assertThat(positionPieceCreated).isEqualTo(positionPieceLoaded);
    }

    @Test
    void updateChessGame() {
        final ChessGame chessGame = springChessGameDao.createChessGame();
        chessGame.move(Position.of("e2"), Position.of("e4"));
        springChessGameDao.updateChessGame(chessGame, "black");

        final ChessGame chessGameUpdated = springChessGameDao.readChessGame();
        final Map<Position, Piece> positionPieceUpdated = chessGameUpdated.generateChessBoard();

        assertThat(positionPieceUpdated.get(Position.of("e4"))).isInstanceOf(Pawn.class);
    }

    @Test
    void deleteChessGame() {
        springChessGameDao.createChessGame();
        springChessGameDao.deleteChessGame();
        assertThatThrownBy(() -> springChessGameDao.readChessGame())
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
