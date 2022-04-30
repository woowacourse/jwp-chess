package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.service.dto.GameDto;
import chess.service.dto.GameStatusDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class JdbcGameDaoTest {

    @Autowired
    private JdbcGameDao jdbcGameDao;

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
        GameDto gameDto = GameDto.of("white", "playing");
        jdbcGameDao.save(1, gameDto);

        jdbcGameDao.removeAll(1);

        assertThat(getGameCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("게임 정보 저장")
    void save() {
        GameDto gameDto = GameDto.of("white", "playing");
        jdbcGameDao.save(1, gameDto);

        assertThat(getGameCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("게임 정보 수정")
    void update() {
        GameDto gameDto = GameDto.of("white", "playing");
        jdbcGameDao.save(1, gameDto);

        GameDto updatedGameDto = GameDto.of("black", "end");
        jdbcGameDao.modify(1, updatedGameDto);

        assertAll(
                () -> assertThat(jdbcGameDao.find(1).getTurn()).isEqualTo("black"),
                () -> assertThat(jdbcGameDao.find(1).getStatus()).isEqualTo("end")
        );
    }

    @Test
    @DisplayName("게임 상태 업데이트")
    void updateStatus() {
        GameDto gameDto = GameDto.of("white", "playing");
        jdbcGameDao.save(1, gameDto);

        GameStatusDto gameStatusDto = GameStatusDto.FINISHED;
        jdbcGameDao.modifyStatus(1, gameStatusDto);

        assertThat(jdbcGameDao.find(1).getStatus()).isEqualTo(gameStatusDto.getName());
    }

    @Test
    @DisplayName("게임 정보 조회")
    void find() {
        GameDto gameDto = GameDto.of("white", "playing");
        jdbcGameDao.save(1, gameDto);

        assertAll(
                () -> assertThat(jdbcGameDao.find(1).getStatus()).isEqualTo("playing"),
                () -> assertThat(jdbcGameDao.find(1).getTurn()).isEqualTo("white")
        );
    }

    private Integer getGameCount() {
        return jdbcTemplate.queryForObject("select count(*) from game", Integer.class);
    }
}
