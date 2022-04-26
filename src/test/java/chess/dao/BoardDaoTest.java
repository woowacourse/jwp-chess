package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.entity.BoardEntity;
import chess.entity.ChessGameEntity;
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

    private static long savedId;

    private BoardDao boardDao;
    private ChessGameDao chessGameDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @BeforeEach
    void setUp() {
        chessGameDao = new ChessGameDao(namedParameterJdbcTemplate);
        boardDao = new BoardDao(namedParameterJdbcTemplate);

        jdbcTemplate.execute("drop table board if exists");
        jdbcTemplate.execute("drop table chess_game if exists");
        jdbcTemplate.execute("create table chess_game\n"
                + "(\n"
                + "    id      int primary key auto_increment,\n"
                + "    name               varchar(20) not null unique,\n"
                + "    is_on              bool        not null,\n"
                + "    team_value_of_turn varchar(20) not null\n"
                + ")");
        jdbcTemplate.execute("create table board\n"
                + "(\n"
                + "    id              int primary key auto_increment,\n"
                + "    chess_game_id         int not null,\n"
                + "    position_column_value varchar(1)  not null,\n"
                + "    position_row_value    int         not null,\n"
                + "    piece_name            varchar(20) not null,\n"
                + "    piece_team_value      varchar(20) not null,\n"
                + "    FOREIGN KEY (chess_game_id) REFERENCES chess_game(id)\n"
                + ")");

        savedId = chessGameDao.save(new ChessGameEntity(0, "juri", true, "BLACK")).longValue();
        jdbcTemplate.execute(
                "insert into board(chess_game_id, position_column_value, position_row_value, piece_name, piece_team_value)"
                        + " values (" + savedId + ", 'a', 2, 'KING', 'BLACK')");
        jdbcTemplate.execute(
                "insert into board(chess_game_id, position_column_value, position_row_value, piece_name, piece_team_value)"
                        + " values (" + savedId + ", 'b', 3, 'QUEEN', 'WHITE')");
    }

    @Test
    @DisplayName("BoardEntity 로 board table 에 저장한다.")
    void save() {
        BoardEntity newBoardEntity = new BoardEntity(savedId, "c", 4, "ROOK", "BLACK");

        boardDao.save(List.of(newBoardEntity));

        List<BoardEntity> boardEntities = boardDao.load(savedId);
        long count = boardEntities.stream()
                .filter(boardEntity -> boardEntity.getChessGameId() == (savedId))
                .count();
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("chess_game_id 를 이용해서 board 를 삭제한다")
    void validateBoardExist() {
        boardDao.delete(savedId);

        assertThatThrownBy(() -> boardDao.load(savedId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] Board 가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("id 로 board 를 조회한다.")
    void load() {
        List<BoardEntity> boardEntities = boardDao.load(savedId);

        long count = boardEntities.stream()
                .filter(boardEntity -> boardEntity.getChessGameId() == (savedId))
                .count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("chess_game_id, position_column_value, position_row_value 를 이용해서 board 를 업데이트 한다")
    void updatePiece() {
        BoardEntity boardEntity = new BoardEntity(savedId, "a", 2, "PAWN", "WHITE");

        boardDao.updatePiece(boardEntity);

        List<BoardEntity> boardEntities = boardDao.load(savedId);
        long count = boardEntities.stream()
                .filter(e -> e.getPositionColumnValue().equals("a") && e.getPositionRowValue() == 2)
                .filter(e -> e.getPieceName().equals("PAWN") && e.getPieceTeamValue().equals("WHITE"))
                .count();
        assertThat(count).isEqualTo(1);
    }
}