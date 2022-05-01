package chess.service;

import chess.dto.ScoreDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ChessGameServiceTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ChessGameService service;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM game");
        jdbcTemplate.execute("ALTER TABLE game AUTO_INCREMENT = 1");
    }

    @Test
    @DisplayName("새로운 게임 생성")
    void create() {
        service.create("name", "password");

        assertThat(jdbcTemplate.queryForObject("SELECT name FROM game WHERE id=1", String.class)).isEqualTo("name");
        assertThat(jdbcTemplate.queryForObject("SELECT password FROM game WHERE id=1", String.class)).isEqualTo("password");

        assertThat(jdbcTemplate.queryForObject("SELECT state FROM current_status WHERE game_id=1", String.class)).isEqualTo("READY");
        assertThat(jdbcTemplate.queryForObject("SELECT turn FROM current_status WHERE game_id=1", String.class)).isEqualTo("WHITE");
    }

    @Test
    @DisplayName("새로운 게임 생성시 이름이 중복된다면 예외 발생")
    void createWithDuplicateName() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");

        assertThatThrownBy(() -> service.create("name", "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("채팅방 이름은 중복될 수 없습니다.");
    }

    @Test
    @DisplayName("삭제할 수 없는 상태라면 예외 발생")
    void deleteWhenWrongState() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        jdbcTemplate.execute("INSERT INTO current_status(game_id,state,turn) VALUES (1,'PLAY','WHITE')");

        assertThatThrownBy(() -> service.delete(1, "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("삭제할 수 없는 상태입니다.");
    }

    @Test
    @DisplayName("올바르지 않은 비밀번호로 삭제를 시도헀다면 예외 발생")
    void deleteWhenWrongPassword() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        jdbcTemplate.execute("INSERT INTO current_status(game_id,state,turn) VALUES (1,'READY','WHITE')");

        assertThatThrownBy(() -> service.delete(1, "wrongPassword"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("올바르지 않은 비밀번호입니다.");

    }

    @Test
    @DisplayName("게임 시작시 상태가 올바르게 변경되었는지 확인")
    void start() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        jdbcTemplate.execute("INSERT INTO current_status(game_id,state,turn) VALUES (1,'READY','WHITE')");

        service.start(1);

        assertThat(jdbcTemplate.queryForObject("SELECT state FROM current_status WHERE game_id=1", String.class)).isEqualTo("PLAY");
    }

    @Test
    @DisplayName("올바르게 움직였는지 확인")
    void move() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        jdbcTemplate.execute("INSERT INTO current_status(game_id,state,turn) VALUES (1,'READY','WHITE')");
        service.start(1);

        service.move(1, "a2", "a4");

        assertThat(jdbcTemplate.queryForObject("SELECT piece_id FROM board WHERE game_id=1 AND position_id=49", Long.class)).isEqualTo(13);
        assertThat(jdbcTemplate.queryForObject("SELECT piece_id FROM board WHERE game_id=1 AND position_id=33", Long.class)).isEqualTo(11);
    }

    @Test
    @DisplayName("게임 종료시 상태가 올바르게 변경되었는지 확인")
    void end() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        jdbcTemplate.execute("INSERT INTO current_status(game_id,state,turn) VALUES (1,'READY','WHITE')");

        service.end(1);

        assertThat(jdbcTemplate.queryForObject("SELECT state FROM current_status WHERE game_id=1", String.class)).isEqualTo("FINISH");
    }

    @Test
    @DisplayName("올바른 점수 결과를 산출하는지 확인")
    void statue() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        jdbcTemplate.execute("INSERT INTO current_status(game_id,state,turn) VALUES (1,'READY','WHITE')");
        service.start(1);

        ScoreDto score = service.status(1);

        assertThat(score.getWhite()).isEqualTo(38);
        assertThat(score.getBlack()).isEqualTo(38);
        assertThat(score.getWinner()).isEqualTo("무승부");
    }

    @Test
    @DisplayName("체스판 정보를 올바르게 반환하는지 확인")
    void getBoardByUnicode() {
        jdbcTemplate.execute("INSERT INTO game(name,password) VALUES ('name','password')");
        jdbcTemplate.execute("INSERT INTO current_status(game_id,state,turn) VALUES (1,'READY','WHITE')");
        service.start(1);

        List result = service.getBoardByUnicode(1);

        List expected = List.of(
                "♜", "♞", "♝", "♛", "♚", "♝", "♞", "♜",
                "♟", "♟", "♟", "♟", "♟", "♟", "♟", "♟",
                "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "",
                "♙", "♙", "♙", "♙", "♙", "♙", "♙", "♙",
                "♖", "♘", "♗", "♕", "♔", "♗", "♘", "♖"
        );

        for (int i = 0; i < 64; i++) {
            assertThat(result.get(i)).isEqualTo(expected.get(i));
        }
    }

}
