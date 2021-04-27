package chess.webdao;

import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.Rook;
import chess.webdto.dao.TeamInfoDto;
import groovy.util.MapEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    @DisplayName("방만들기 - 만들어진 row count 확인")
    void createRoom(){
        long rowNum = mysqlChessDao.createRoom("white", true);
        assertThat(rowNum).isEqualTo(1L);
    }

    @Test
    @DisplayName("보드 만들기 - 만들기 return void")
    void createBoard(){
        // given
        TeamInfoDto teamInfoDto = new TeamInfoDto("white", Position.of("a1"), new Rook(), 1L);

        // when
        createRoom();
        mysqlChessDao.createBoard(teamInfoDto);
    }





}