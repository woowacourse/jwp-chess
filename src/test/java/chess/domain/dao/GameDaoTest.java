package chess.domain.dao;

import chess.domain.dto.GameDto;
import chess.domain.game.board.ChessBoard;
import chess.domain.game.board.ChessBoardFactory;
import chess.domain.game.status.Playing;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class GameDaoTest {

    @Autowired
    private GameJdbcTemplateDao gameDao;
    private final ChessBoard chessBoard = initTestChessBoard();

    private ChessBoard initTestChessBoard() {
        ChessBoard chessBoard = ChessBoardFactory.initBoard();
        chessBoard.changeStatus(new Playing());
        return chessBoard;
    }

    private int gameSave() {
        return gameDao.save(chessBoard);
    }

    @BeforeEach
    void setup() {
        gameDao.deleteAll();
    }

    @Test
    @DisplayName("게임을 저장한다.")
    void save() {
        assertDoesNotThrow(() -> gameSave());
    }

    @Test
    @DisplayName("가장 최근 게임을 불러온다")
    void findLastGame() {
        //given
        gameSave();
        gameSave();
        //when
        int actual = gameDao.findLastGameId();
        //then
        assertThat(actual).isEqualTo(2);
    }

    @Test
    @DisplayName("id로 게임을 불러온다")
    void findById() {
        //given
        gameSave();
        //when
        GameDto actual = gameDao.findById(1);
        //then
        assertThat(actual.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("가장 최근 게임을 삭제한다.")
    void delete() {
        //given
        gameSave();
        gameSave();
        //when
        gameDao.delete();
        //then
        assertThat(gameDao.findLastGameId()).isEqualTo(1);
    }

}
