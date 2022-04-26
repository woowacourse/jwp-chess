package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.FakeBoardDao;
import chess.dao.FakeGameDao;
import chess.domain.board.Board;
import chess.domain.board.coordinate.Coordinate;
import chess.domain.game.StatusCalculator;
import chess.domain.piece.Piece;
import chess.dto.MoveRequestDto;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessServiceTest {

    private FakeBoardDao fakeBoardDao = new FakeBoardDao();
    private ChessService chessService = new ChessService(fakeBoardDao, new FakeGameDao());
    private int gameId;

    @BeforeEach
    void init() {
        chessService.saveGame("a", "b", "WHITE");
        gameId = chessService.findGameIdByUserName("a", "b");
        chessService.saveBoard(Board.create(), gameId);
    }

    @AfterEach
    void clear() {
        fakeBoardDao.deleteByGameId(gameId);
        chessService.deleteGameByGameId(gameId);
    }

    @Test
    @DisplayName("게임 아이디에 해당되는 보드를 찾는다.")
    void findBoardByGameId() {
        Board board = chessService.findBoardByGameId(gameId);
        assertThat(board.toMap()).containsAllEntriesOf(Board.create().toMap());
    }

    @Test
    @DisplayName("새로운 유저 이름을 받으면 게임을 새로 시작한다.")
    void start() {
        chessService.start("c", "d");
        int gameIdByUserName = chessService.findGameIdByUserName("c", "d");

        assertThat(gameIdByUserName).isEqualTo(2);
    }

    @Test
    @DisplayName("기물을 이동시킨다.")
    void move() {
        MoveRequestDto moveRequestDto = new MoveRequestDto("b1", "c3");
        chessService.move(gameId, moveRequestDto);

        Board board = chessService.findBoardByGameId(gameId);

        assertThat(board.findPiece(Coordinate.of("c3"))).isEqualTo(Piece.of("KNIGHT", "WHITE"));
    }

    @Test
    void findGameIdByUserName() {
        int gameIdByUserName = chessService.findGameIdByUserName("a", "b");
        assertThat(gameIdByUserName).isEqualTo(1);
    }

    @Test
    void createStatus() {
        StatusCalculator status = chessService.createStatus(gameId);
        Map<String, Double> score = status.createStatus();

        assertThat(score.get("WHITE")).isEqualTo(38);
    }
}
