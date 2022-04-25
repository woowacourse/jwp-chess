package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.repository.entity.BoardEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/settingForTest.sql")
public class BoardDaoTest {

    private static final String SAVED_NAME = "test";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private BoardDao boardDao;
    private ChessGameDao chessGameDao;

    @BeforeEach
    void setUp() {
        boardDao = new BoardDao(namedParameterJdbcTemplate);
        chessGameDao = new ChessGameDao(namedParameterJdbcTemplate);
    }

    @Test
    @DisplayName("BoardEntity 로 board table 에 저장한다.")
    void save() {
        int chessGameId = chessGameDao.findChessGameIdByName(SAVED_NAME);
        BoardEntity newBoardEntity = new BoardEntity(chessGameId, "c", 4, "ROOK", "BLACK");

        boardDao.save(List.of(newBoardEntity));

        List<BoardEntity> boardEntities = boardDao.load(chessGameId);
        long count = boardEntities.stream()
                .filter(boardEntity -> boardEntity.getChessGameId() == chessGameId)
                .count();
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("조회할 board 가 존재하지 않을 때 예외를 발생시킨다.")
    void validateBoardExist() {
        assertThatThrownBy(() -> boardDao.load(2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] Board 가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("chess_game_id 으로 board 를 조회한다.")
    void load() {
        int chessGameId = chessGameDao.findChessGameIdByName(SAVED_NAME);

        List<BoardEntity> boardEntities = boardDao.load(chessGameId);

        long count = boardEntities.stream()
                .filter(boardEntity -> boardEntity.getChessGameId() == chessGameId)
                .count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("name, position_column_value, position_row_value 를 이용해서 board 를 업데이트 한다")
    void updatePiece() {
        int chessGameId = chessGameDao.findChessGameIdByName(SAVED_NAME);
        BoardEntity boardEntity = new BoardEntity(chessGameId, "a", 2, "PAWN", "WHITE");

        boardDao.updatePiece(boardEntity);

        List<BoardEntity> boardEntities = boardDao.load(chessGameId);
        long count = boardEntities.stream()
                .filter(e -> e.getPositionColumnValue().equals("a") && e.getPositionRowValue() == 2)
                .filter(e -> e.getPieceName().equals("PAWN") && e.getPieceTeamValue().equals("WHITE"))
                .count();
        assertThat(count).isEqualTo(1);
    }
}