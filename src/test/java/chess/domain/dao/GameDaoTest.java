package chess.domain.dao;

import chess.domain.dto.GameDto;
import chess.domain.game.board.ChessBoard;
import chess.domain.game.board.ChessBoardFactory;
import chess.domain.game.status.Playing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class GameDaoTest {

    private final ChessBoard chessBoard = initTestChessBoard();
    @Autowired
    private GameJdbcTemplateDao gameDao;

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
