package chess.webdao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
class MysqlChessDaoTest {
    private MysqlChessDao mysqlChessDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp(){
        this.mysqlChessDao = new MysqlChessDao(jdbcTemplate);
    }

    @AfterEach
    void cleanUp(){
        mysqlChessDao.deleteBoardByRoomId(1L);
        mysqlChessDao.deleteBoardByRoomId(1L);
    }

    @Test
    void createRoom(){
        long rowNum = mysqlChessDao.createRoom("white", true);
        assertThat(rowNum).isEqualTo(1L);
    }




}