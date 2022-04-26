package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dto.GameDto;
import chess.dto.GameStatusDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class GameDaoImplTest {

    @Autowired
    private GameDaoImpl gameDaoImpl;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
        jdbcTemplate.execute("create table game\n"
                + "(\n"
                + "    game_id int         not null auto_increment,\n"
                + "    turn    varchar(20) not null,\n"
                + "    status    varchar(20) not null,\n"
                + "    primary key (game_id)\n"
                + ");");
    }

    @Test
    @DisplayName("전체 게임 데이터 삭제")
    void removeAll() {
        GameDto gameDto = new GameDto("white", "playing");
        gameDaoImpl.saveGame(gameDto);

        gameDaoImpl.removeAll();

        assertThat(getGameCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("게임 정보 저장")
    void save() {
        GameDto gameDto = new GameDto("white", "playing");
        gameDaoImpl.saveGame(gameDto);

        assertThat(getGameCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("게임 정보 수정")
    void update() {
        GameDto gameDto = new GameDto("white", "playing");
        gameDaoImpl.saveGame(gameDto);

        GameDto updatedGameDto = new GameDto("black", "end");
        gameDaoImpl.updateGame(updatedGameDto);

        assertAll(
                () -> assertThat(gameDaoImpl.findGame().getTurn()).isEqualTo("black"),
                () -> assertThat(gameDaoImpl.findGame().getStatus()).isEqualTo("end")
        );
    }

    @Test
    @DisplayName("게임 상태 업데이트")
    void updateStatus() {
        GameDto gameDto = new GameDto("white", "playing");
        gameDaoImpl.saveGame(gameDto);

        GameStatusDto gameStatusDto = GameStatusDto.FINISHED;
        gameDaoImpl.updateStatus(gameStatusDto);

        assertThat(gameDaoImpl.findGame().getStatus()).isEqualTo(gameStatusDto.getName());
    }

    @Test
    @DisplayName("게임 정보 조회")
    void find() {
        GameDto gameDto = new GameDto("white", "playing");
        gameDaoImpl.saveGame(gameDto);

        assertAll(
                () -> assertThat(gameDaoImpl.findGame().getStatus()).isEqualTo("playing"),
                () -> assertThat(gameDaoImpl.findGame().getTurn()).isEqualTo("white")
        );
    }

    private Integer getGameCount() {
        return jdbcTemplate.queryForObject("select count(*) from game", Integer.class);
    }
}
