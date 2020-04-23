package wooteco.chess.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.dao.ChessDao;
import wooteco.chess.dao.InMemoryChessDao;
import wooteco.chess.dao.MySqlChessDao;
import wooteco.chess.dto.Commands;

import static org.assertj.core.api.Assertions.assertThat;

class ChessDaoTest {
    ChessDao chessDao = new InMemoryChessDao();

    @BeforeEach
    void setUp() {
        if (MySqlConnector.getConnection() != null) {
            chessDao = new MySqlChessDao();
        }
        chessDao.addCommand(new Commands("move a2 a4"));
        chessDao.addCommand(new Commands("move a7 a5"));
    }

    @DisplayName("테이블 행 삭제")
    @Test
    void clearCommands() {
        chessDao.clearCommands();

        assertThat(chessDao.selectCommands().size()).isZero();
    }

    @DisplayName("전체 행 선택")
    @Test
    void selectCommands() {
        assertThat(chessDao.selectCommands().size()).isEqualTo(2);
    }
}