package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dto.GameDto;
import chess.dto.GameStatusDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class GameDaoSpringImplTest {

    private GameDaoSpringImpl gameDaoSpringImpl;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDaoSpringImpl = new GameDaoSpringImpl(jdbcTemplate);
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
        gameDaoSpringImpl.save(gameDto);

        gameDaoSpringImpl.removeAll();

        assertThat(getGameCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("게임 정보 저장")
    void save() {
        GameDto gameDto = new GameDto("white", "playing");
        gameDaoSpringImpl.save(gameDto);

        assertThat(getGameCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("게임 정보 수정")
    void update() {
        GameDto gameDto = new GameDto("white", "playing");
        gameDaoSpringImpl.save(gameDto);

        GameDto updatedGameDto = new GameDto("black", "end");
        gameDaoSpringImpl.update(updatedGameDto);

        assertAll(
                () -> assertThat(gameDaoSpringImpl.find().getTurn()).isEqualTo("black"),
                () -> assertThat(gameDaoSpringImpl.find().getStatus()).isEqualTo("end")
        );
    }

    @Test
    @DisplayName("게임 상태 업데이트")
    void updateStatus() {
        GameDto gameDto = new GameDto("white", "playing");
        gameDaoSpringImpl.save(gameDto);

        GameStatusDto gameStatusDto = GameStatusDto.FINISHED;
        gameDaoSpringImpl.updateStatus(gameStatusDto);

        assertThat(gameDaoSpringImpl.find().getStatus()).isEqualTo(gameStatusDto.getName());
    }

    @Test
    @DisplayName("게임 정보 조회")
    void find() {
        GameDto gameDto = new GameDto("white", "playing");
        gameDaoSpringImpl.save(gameDto);

        assertAll(
                () -> assertThat(gameDaoSpringImpl.find().getStatus()).isEqualTo("playing"),
                () -> assertThat(gameDaoSpringImpl.find().getTurn()).isEqualTo("white")
        );
    }

    private Integer getGameCount() {
        return jdbcTemplate.queryForObject("select count(*) from game", Integer.class);
    }
}
