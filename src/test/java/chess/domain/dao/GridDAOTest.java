package chess.domain.dao;

import chess.dao.GridDAO;
import chess.dto.GridDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class GridDAOTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GridDAO gridDao;
    private long roomId;
    private long gridId;

    @BeforeEach
    public void setup() {
        jdbcTemplate.execute("TRUNCATE TABLE piece");
        jdbcTemplate.execute("TRUNCATE TABLE grid");
        jdbcTemplate.execute("TRUNCATE TABLE room");
        roomId = 1L;
        gridId = gridDao.createGrid(roomId);
    }

    @Test
    @DisplayName("gridId로 해당 grid를 찾는 지 테스트")
    public void findGridByGridId() {
        GridDto gridDto = gridDao.findGridByGridId(gridId);
        assertThat(gridDto.getGridId()).isEqualTo(gridId);
    }

    @Test
    @DisplayName("Grid를 생성하면 초기 값이 잘 셋팅되는 지 테스트")
    public void findGridByGridId_CheckInitValues() {
        GridDto gridDto = gridDao.findGridByGridId(gridId);
        assertThat(gridDto.getGridId()).isEqualTo(gridId);
        assertThat(gridDto.getRoomId()).isEqualTo(roomId);
        assertThat(gridDto.getIsBlackTurn()).isEqualTo(false);
        assertThat(gridDto.getIsFinished()).isEqualTo(false);
        assertThat(gridDto.getIsStarted()).isEqualTo(false);
    }

    @Test
    @DisplayName("roomId로 해당 grid를 찾는 지 테스트")
    public void findRecentGridByRoomId() {
        GridDto gridDto = gridDao.findGridByGridId(roomId);
        assertThat(gridDto.getGridId()).isEqualTo(gridId);
    }

    @Test
    @DisplayName("Grid의 상태를 Starting으로 바꾸는 지 테스트")
    public void changeToStarting() {
        gridDao.changeToStarting(gridId);
        GridDto gridDto = gridDao.findGridByGridId(gridId);
        assertThat(gridDto.getIsStarted()).isTrue();
    }

    @Test
    @DisplayName("Grid의 차례를 정상적으로 바꾸는 지 테스트")
    public void changeTurn() {
        gridDao.changeTurn(gridId, false);
        GridDto gridDto = gridDao.findGridByGridId(gridId);
        assertThat(gridDto.getIsBlackTurn()).isFalse();

        gridDao.changeTurn(gridId, true);
        GridDto gridDto2 = gridDao.findGridByGridId(gridId);
        assertThat(gridDto2.getIsBlackTurn()).isTrue();
    }

    @Test
    @DisplayName("Grid를 끝난 상태로 정상적으로 바꾸는 지 테스트")
    public void changeToFinished() {
        gridDao.changeToFinished(gridId);
        GridDto gridDto = gridDao.findGridByGridId(gridId);
        assertThat(gridDto.getIsFinished()).isTrue();
    }
}