package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dto.GameDto;
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
        jdbcTemplate.execute("DROP TABLE piece IF EXISTS");
        jdbcTemplate.execute("create table piece\n"
                + "(\n"
                + "    position varchar(5)  not null,\n"
                + "    type     varchar(10) not null,\n"
                + "    color    varchar(20) not null,\n"
                + "    primary key (position)\n"
                + ");");
    }

    @Test
    @DisplayName("전체 게임 데이터 삭제")
    void removeAll() {
        jdbcTemplate.execute("insert into game (turn, status) values ('white', 'playing')");

        gameDaoSpringImpl.removeAll();

        assertThat(jdbcTemplate.queryForObject("select count(*) from game", Integer.class)).isEqualTo(0);
    }

    @Test
    @DisplayName("게임 정보 저장")
    void save() {
        GameDto gameDto = new GameDto("white", "playing");
        gameDaoSpringImpl.save(gameDto);

        assertThat(jdbcTemplate.queryForObject("select count(*) from game", Integer.class)).isEqualTo(1);
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
}
