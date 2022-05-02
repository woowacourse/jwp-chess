package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.BoardDao;
import chess.dao.FakeBoard;
import chess.dao.MockBoardDao;
import chess.dao.MockPieceDao;
import chess.dao.PieceDao;
import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.dto.CreateBoardDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChessGameServiceTest {
    private BoardDao boardDao;
    private PieceDao pieceDao;

    @BeforeEach
    void init() {
        boardDao = new MockBoardDao();
        pieceDao = new MockPieceDao();
    }

    @Test
    @DisplayName("이름과 비밀번호로 보드를 만들 수 있다.")
    void initial_start() {
        int id = boardDao.makeBoard(
                new Board(BoardInitializer.createBoard(), Color.WHITE, new CreateBoardDto("name", "password")));
        pieceDao.save(BoardInitializer.createBoard(), id);

        assertThat(id).isEqualTo(1);
    }

    @Test
    @DisplayName("board의 turn을 update할 수 있다.")
    void updateBoardTurn() {
        //given
        int id = boardDao.makeBoard(
                new Board(BoardInitializer.createBoard(), Color.WHITE, new CreateBoardDto("name", "password")));
        pieceDao.save(BoardInitializer.createBoard(), id);

        //when
        boardDao.updateTurn(Color.BLACK, id);

        //then
        assertThat(boardDao.findTurn(id)).isEqualTo(Color.BLACK);
    }

    @Test
    @DisplayName("id값으로 보드를 가져올 수 있다.")
    void getBoard() {
        boardDao.makeBoard(
                new Board(BoardInitializer.createBoard(), Color.WHITE, new CreateBoardDto("name", "password")));
        pieceDao.save(BoardInitializer.createBoard(), 1);

        assertThat(pieceDao.load(1)).containsAllEntriesOf(BoardInitializer.createBoard());
    }

    @Test
    @DisplayName("id값으로 보드의 현재 turn을 가져올 수 있다.")
    void getTurn() {
        boardDao.makeBoard(
                new Board(BoardInitializer.createBoard(), Color.WHITE, new CreateBoardDto("name", "password")));
        assertThat(boardDao.findTurn(1)).isEqualTo(Color.WHITE);
    }
}
