package chess.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import chess.dto.ChessGameDto;

@JdbcTest
class ChessGameDaoImplTest {

    private ChessGameDaoImpl chessGameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessGameDao = new ChessGameDaoImpl(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS game");
        jdbcTemplate.execute("CREATE TABLE game("
            + "id int not null AUTO_INCREMENT PRIMARY KEY,"
            + "title varchar(50) not null,"
            + "password varchar(20) not null"
            + ");");

        jdbcTemplate.update("insert into game (title, password) values (?, ?)",
            "제목", "password");
    }

    @DisplayName("체스 게임방 데이터를 삽입한다.")
    @Test
    void insert() {
        ChessGameDto chessGameDto = new ChessGameDto("제목2", "password");

        chessGameDao.insert(chessGameDto);

        assertThat(chessGameDao.find(2L).getTitle()).isEqualTo("제목2");
    }

    @DisplayName("체스 게임 방 데이터를 삭제한다.")
    @Test
    void delete() {
        assertThat(chessGameDao.delete(1L)).isEqualTo(1);
    }

    @DisplayName("체스 게임 방 데이터를 모두 가져온다.")
    @Test
    void findAll() {
        assertThat(chessGameDao.findAll().size()).isEqualTo(1);
    }
}
