package chess.repository.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.repository.entity.BoardEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@JdbcTest
public class BoardDaoTest {

    private static final String SAVED_NAME = "test";

    private BoardDao boardDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        boardDao = new BoardDao(namedParameterJdbcTemplate);

        jdbcTemplate.execute("drop table board if exists");
        jdbcTemplate.execute("create table board\n"
                + "(\n"
                + "    board_id              int primary key auto_increment,\n"
                + "    name                  varchar(20) not null,\n"
                + "    position_column_value varchar(1)  not null,\n"
                + "    position_row_value    int         not null,\n"
                + "    piece_name            varchar(20) not null,\n"
                + "    piece_team_value      varchar(20) not null\n"
                + ")");

        jdbcTemplate.execute(
                "insert into board(name, position_column_value, position_row_value, piece_name, piece_team_value)"
                        + " values ('" + SAVED_NAME + "', 'a', 2, 'KING', 'BLACK')");
        jdbcTemplate.execute(
                "insert into board(name, position_column_value, position_row_value, piece_name, piece_team_value)"
                        + " values ('" + SAVED_NAME + "', 'b', 3, 'QUEEN', 'WHITE')");
    }

    @Test
    @DisplayName("BoardEntity 로 board table 에 저장한다.")
    void save() {
        BoardEntity newBoardEntity = new BoardEntity(SAVED_NAME, "c", 4, "ROOK", "BLACK");

        boardDao.save(List.of(newBoardEntity));

        List<BoardEntity> boardEntities = boardDao.load(SAVED_NAME);
        long count = boardEntities.stream()
                .filter(boardEntity -> boardEntity.getName().equals(SAVED_NAME))
                .count();
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("name 을 이용해서 board 를 삭제한다")
    void delete() {
        jdbcTemplate.execute(
                "insert into board(name, position_column_value, position_row_value, piece_name, piece_team_value)"
                        + " values ('other', 'b', 3, 'QUEEN', 'WHITE')"
        );

        boardDao.delete(SAVED_NAME);

        List<BoardEntity> boardEntities = boardDao.load("other");
        long count = boardEntities.stream()
                .filter(boardEntity -> boardEntity.getName().equals("other"))
                .count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("조회할 board 가 존재하지 않을 때 예외를 발생시킨다.")
    void validateBoardExist() {
        boardDao.delete(SAVED_NAME);

        assertThatThrownBy(() -> boardDao.load(SAVED_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] Board 가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("name 으로 board 를 조회한다.")
    void load() {
        List<BoardEntity> boardEntities = boardDao.load(SAVED_NAME);

        long count = boardEntities.stream()
                .filter(boardEntity -> boardEntity.getName().equals(SAVED_NAME))
                .count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("name, position_column_value, position_row_value 를 이용해서 board 를 업데이트 한다")
    void updatePiece() {
        BoardEntity boardEntity = new BoardEntity(SAVED_NAME, "a", 2, "PAWN", "WHITE");

        boardDao.updatePiece(boardEntity);

        List<BoardEntity> boardEntities = boardDao.load(SAVED_NAME);
        long count = boardEntities.stream()
                .filter(e -> e.getPositionColumnValue().equals("a") && e.getPositionRowValue() == 2)
                .filter(e -> e.getPieceName().equals("PAWN") && e.getPieceTeamValue().equals("WHITE"))
                .count();
        assertThat(count).isEqualTo(1);
    }
}