package chess.webdao;

import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import chess.webdto.dao.BoardInfosDto;
import chess.webdto.dao.TeamInfoDto;
import chess.webdto.dao.TurnDto;
import groovy.util.MapEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    @DisplayName("조회 - 방번호로 현재 턴")
    void selectTurnByRoomId(){
        mysqlChessDao.createRoom("black", true);

        TurnDto turnDto = mysqlChessDao.selectTurnByRoomId(1L);

        assertThat(turnDto.getTurn()).isEqualTo("black");
    }

    @Test
    @DisplayName("조회 -  해당 방번호로 되어있는 보드 정보의 리스트 크기")
    void selectBoardInfosByRoomId() {
        // given
        createRoom();

        List<TeamInfoDto> teams = new ArrayList<>();
        teams.add(new TeamInfoDto("white", Position.of("a2"), new Queen(), 1L));
        teams.add(new TeamInfoDto("black", Position.of("b3"), new Rook(), 1L));

        for(TeamInfoDto team: teams){
            mysqlChessDao.createBoard(team);
        }

        // when
        List<BoardInfosDto> boardInfosDtos = mysqlChessDao.selectBoardInfosByRoomId(1L);

        // then
        assertThat(boardInfosDtos).hasSize(2);
    }

    @Test
    @DisplayName("업데이트 - room 턴정보 변경")
    void changeTurnByRoomId() {
        mysqlChessDao.createRoom("white", true);

        TurnDto before = mysqlChessDao.selectTurnByRoomId(1L);
        assertThat(before.getTurn()).isEqualTo("white");

        mysqlChessDao.changeTurnByRoomId("black", true, 1L);

        TurnDto after = mysqlChessDao.selectTurnByRoomId(1L);
        assertThat(after.getTurn()).isEqualTo("black");
    }
}