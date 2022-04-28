package chess.domain.dao;

import chess.domain.game.board.ChessBoard;
import chess.domain.game.board.ChessBoardFactory;
import chess.domain.game.status.Playing;
import chess.service.dto.GameDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class GameDaoTest {

    private final ChessBoard chessBoard = initTestChessBoard();
    private final GameDao gameDao;

    public GameDaoTest(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    private ChessBoard initTestChessBoard() {
        ChessBoard chessBoard = ChessBoardFactory.initBoard();
        chessBoard.changeStatus(new Playing());
        return chessBoard;
    }

    private int gameCreate() {
        return gameDao.create(chessBoard, "test", "jh1212");
    }

    @BeforeEach
    void setup() {
        gameDao.deleteAll();
    }

    @Test
    @DisplayName("게임을 저장한다.")
    void save() {
        assertDoesNotThrow(() -> gameCreate());
    }


    @Test
    @DisplayName("id로 게임을 불러온다")
    void findById() {
        //given
        gameCreate();
        //when
        GameDto actual = gameDao.findById(1);
        //then
        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual.getTitle()).isEqualTo("test");
    }

    @Test
    @DisplayName("지정 아이디를 가진 게임을 삭제한다.")
    void delete() {
        //given
        gameCreate();
        //when
        gameDao.delete(1);
        //then
        assertThat(gameDao.findById(1)).isNull();
    }

}
