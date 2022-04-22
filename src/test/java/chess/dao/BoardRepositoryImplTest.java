package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.board.RegularRuleSetup;
import chess.web.dto.GameStateDto;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@JdbcTest
public class BoardRepositoryImplTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private BoardRepository boardRepository;
    int roomId;

    @BeforeEach
    void init() {
        boardRepository = new BoardRepositoryImpl(dataSource, jdbcTemplate);
        RoomRepository roomRepository = new RoomRepositoryImpl(dataSource, jdbcTemplate);
        roomId = roomRepository.save("summer");
    }

    @DisplayName("체스판 저장")
    @Test
    void save() {
        Board board = new Board(new RegularRuleSetup());
        int id = boardRepository.save(roomId, GameStateDto.from(board));
        assertThat(id).isGreaterThan(0);
    }

    @Test
    @DisplayName("boardIdfh 차례를 조회한다.")
    void getTurn() {
        Board board = new Board(new RegularRuleSetup());
        int boardId = boardRepository.save(roomId, GameStateDto.from(board));
        Color findTurn = boardRepository.getTurn(boardId);
        assertThat(findTurn).isEqualTo(board.getTurn());
    }

    @Test
    @DisplayName("roomId로 board 조회")
    void getBoardByRoomId() {
        Board board = new Board(new RegularRuleSetup());
        int boardId = boardRepository.save(roomId, GameStateDto.from(board));
        int findBoardId = boardRepository.getBoardIdByRoom(roomId);
        assertThat(boardId).isEqualTo(findBoardId);
    }

    @Test
    @DisplayName("없는 roomId로 조회시 예외 발생")
    void getBoardByRoomIdException() {
        assertThatThrownBy(() -> boardRepository.getBoardIdByRoom(roomId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("새로운 turn을 업데이트")
    void updateTurn() {
        Board board = new Board(new RegularRuleSetup());
        int boardId = boardRepository.save(roomId, GameStateDto.from(board));
        board.loadTurn(Color.BLACK);
        boardRepository.updateTurn(boardId, GameStateDto.from(board));
        assertThat(board.getTurn()).isEqualTo(boardRepository.getTurn(boardId));
    }

    @Test
    @DisplayName("roomId로 체스판을 삭제한다.")
    void deleteByRoomId() {
        Board board = new Board(new RegularRuleSetup());
        boardRepository.save(roomId, GameStateDto.from(board));
        boardRepository.deleteByRoom(roomId);

        assertThatThrownBy(() -> boardRepository.getBoardIdByRoom(roomId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
