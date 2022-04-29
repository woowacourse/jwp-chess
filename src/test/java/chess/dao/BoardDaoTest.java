package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.fake.FakeBoardDao;
import chess.domain.board.Board;
import chess.domain.board.strategy.WebBasicBoardStrategy;
import chess.domain.piece.Blank;
import chess.domain.piece.WhitePawn;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardDaoTest {

    private BoardDao boardDao;

    @BeforeEach
    void init() {
        boardDao = new FakeBoardDao();
        Board board = new Board();
        board.initBoard(new WebBasicBoardStrategy());
        boardDao.create(board.toMap(), 1);
    }

    @Test
    @DisplayName("기본 보드를 가져온다.")
    void getBoard() {
        Board board = new Board();
        board.initBoard(new WebBasicBoardStrategy());
        assertThat(toMap(boardDao.getBoard(1))).isEqualTo(board.toMap());
    }

    @Test
    @DisplayName("이동 업데이트 로직을 확인한다.")
    void update() {
        boardDao.update("a3", "white_pawn", 1);
        boardDao.update("a2", "blank", 1);

        Board board = new Board();
        board.initBoard(new WebBasicBoardStrategy());
        board.move(new Position("a3"), new WhitePawn());
        board.move(new Position("a2"), new Blank());

        assertThat(toMap(boardDao.getBoard(1))).isEqualTo(board.toMap());
    }

    @Test
    @DisplayName("리셋을 확인한다.")
    void reset() {
        boardDao.update("a3", "white_pawn", 1);
        boardDao.update("a2", "blank", 1);

        Board board = new Board();

        boardDao.reset(board.toMap(), 1);

        assertThat(toMap(boardDao.getBoard(1))).isEqualTo(board.toMap());
    }

    private Map<String, String> toMap(List<BoardDto> data) {
        return data.stream().collect(Collectors.toMap(BoardDto::getPosition, BoardDto::getPiece));
    }
}
