package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.FakeBoardDao;
import chess.dao.FakeGameDao;
import chess.domain.board.Board;
import chess.domain.board.coordinate.Coordinate;
import chess.domain.game.StatusCalculator;
import chess.domain.piece.Piece;
import chess.dto.GameCreateDto;
import chess.dto.GameDeleteDto;
import chess.dto.GameDto;
import chess.dto.MoveRequestDto;
import java.util.List;
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
        gameId = chessService.start(new GameCreateDto("test", "password"));
    }

    @AfterEach
    void clear() {
        fakeBoardDao.deleteByGameId(gameId);
        chessService.deleteGameByGameId(new GameDeleteDto(gameId, "password"));
    }

    @Test
    @DisplayName("게임 아이디에 해당되는 보드를 찾는다.")
    void findBoardByGameId() {
        Board board = chessService.findBoardByGameId(gameId);
        assertThat(board.toMap()).containsAllEntriesOf(Board.create().toMap());
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
    @DisplayName("게임 결과를 반환한다.")
    void createStatus() {
        StatusCalculator status = chessService.createStatus(gameId);
        Map<String, Double> score = status.createStatus();

        assertThat(score.get("WHITE")).isEqualTo(38);
    }

    @Test
    @DisplayName("모든 게임 리스트를 반환한다.")
    void findGames() {
        List<GameDto> games = chessService.findGames();

        assertThat(games.size()).isEqualTo(1);
    }
}
