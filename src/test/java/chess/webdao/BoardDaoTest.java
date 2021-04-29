package chess.webdao;

import chess.domain.Position;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import chess.webdto.dao.BoardInfosDto;
import chess.webdto.dao.TeamInfoDto;
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

@JdbcTest
@ActiveProfiles("test")
class BoardDaoTest {
    private BoardDao boardDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.boardDao = new BoardDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS board;\n" +
                "DROP TABLE IF EXISTS room;\n" +
                "CREATE TABLE room(" +
                "    room_id    BIGINT      NOT NULL AUTO_INCREMENT,\n" +
                "    turn       VARCHAR(16) NOT NULL,\n" +
                "    is_playing BOOLEAN     NOT NULL,\n" +
                "    name       VARCHAR(16) NOT NULL,\n" +
                "    password   VARCHAR(16),\n" +
                "    create_at  DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
                "    PRIMARY KEY (room_id)\n" +
                ");\n" +
                "CREATE TABLE board(" +
                "    board_id       BIGINT      NOT NULL AUTO_INCREMENT,\n" +
                "    team           VARCHAR(16) NOT NULL,\n" +
                "    position       VARCHAR(16) NOT NULL,\n" +
                "    piece          VARCHAR(16) NOT NULL,\n" +
                "    is_first_moved BOOLEAN     NOT NULL,\n" +
                "    room_id        BIGINT      NOT NULL,\n" +
                "    PRIMARY KEY (board_id),\n" +
                "    FOREIGN KEY (room_id) REFERENCES room (room_id)\n" +
                ");");
        jdbcTemplate.execute("INSERT INTO room (turn, is_playing, name) VALUES ('white', true, 'sample' ) ");

    }

    @Test
    @DisplayName("보드 만들기 - 만들기 return void")
    void createBoard() {
        // given
        TeamInfoDto teamInfoDto = new TeamInfoDto("white", Position.of("a1"), new Rook(), 1L);

        // when
        boardDao.createBoard(teamInfoDto);
    }


    @Test
    @DisplayName("조회 -  해당 방번호로 되어있는 보드 정보의 리스트 크기")
    void selectBoardInfosByRoomId() {
        // given
        List<TeamInfoDto> teams = new ArrayList<>();
        teams.add(new TeamInfoDto("white", Position.of("a2"), new Queen(), 1L));
        teams.add(new TeamInfoDto("black", Position.of("b3"), new Rook(), 1L));

        for (TeamInfoDto team : teams) {
            boardDao.createBoard(team);
        }

        // when
        List<BoardInfosDto> boardInfosDtos = boardDao.selectBoardInfosByRoomId(1L);

        // then
        assertThat(boardInfosDtos).hasSize(2);
    }

    @Test
    @DisplayName("보드판 삭제")
    void deleteBoardByRoomId(){
        // given
        TeamInfoDto teamInfoDto = new TeamInfoDto("white", Position.of("a1"), new Rook(), 1L);
        boardDao.createBoard(teamInfoDto);
        assertThat(boardDao.selectBoardInfosByRoomId(1L)).hasSize(1);
        // when
        boardDao.deleteBoardByRoomId(1L);
        //then
        assertThat(boardDao.selectBoardInfosByRoomId(1L)).hasSize(0);
    }

}