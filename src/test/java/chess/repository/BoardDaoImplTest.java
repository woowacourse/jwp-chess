package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.board.RegularRuleSetup;
import chess.domain.GameState;
import chess.repository.entity.RoomEntity;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@JdbcTest
public class BoardDaoImplTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private BoardDao boardDao;
    int roomId;

    @BeforeEach
    void init() {
        boardDao = new BoardDaoImpl(dataSource, jdbcTemplate);
        RoomDao roomDao = new RoomDaoImpl(dataSource, jdbcTemplate);
        roomId = roomDao.save(new RoomEntity("summer", "summer"));
    }

    @DisplayName("체스판 저장")
    @Test
    void save() {
        Board board = new Board(new RegularRuleSetup());
        int id = boardDao.save(roomId, GameState.from(board));
        assertThat(id).isGreaterThan(0);
    }

    @Test
    @DisplayName("boardIdfh 차례를 조회한다.")
    void getTurn() {
        Board board = new Board(new RegularRuleSetup());
        int boardId = boardDao.save(roomId, GameState.from(board));
        Color findTurn = boardDao.getTurn(boardId);
        assertThat(findTurn).isEqualTo(board.getTurn());
    }

    @Test
    @DisplayName("roomId로 board 조회")
    void getBoardByRoomId() {
        Board board = new Board(new RegularRuleSetup());
        int boardId = boardDao.save(roomId, GameState.from(board));
        int findBoardId = boardDao.getBoardIdByRoom(roomId);
        assertThat(boardId).isEqualTo(findBoardId);
    }

    @Test
    @DisplayName("없는 roomId로 조회시 예외 발생")
    void getBoardByRoomIdException() {
        assertThatThrownBy(() -> boardDao.getBoardIdByRoom(roomId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("새로운 turn을 업데이트")
    void updateTurn() {
        Board board = new Board(new RegularRuleSetup());
        int boardId = boardDao.save(roomId, GameState.from(board));
        board.loadTurn(Color.BLACK);
        boardDao.updateTurn(boardId, GameState.from(board));
        assertThat(board.getTurn()).isEqualTo(boardDao.getTurn(boardId));
    }

    @Test
    @DisplayName("roomId로 체스판을 삭제한다.")
    void deleteByRoomId() {
        Board board = new Board(new RegularRuleSetup());
        boardDao.save(roomId, GameState.from(board));
        boardDao.deleteByRoom(roomId);

        assertThatThrownBy(() -> boardDao.getBoardIdByRoom(roomId))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
