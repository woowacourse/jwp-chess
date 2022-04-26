package chess.web.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;
import chess.web.dao.ChessBoardDao;
import chess.web.dao.RoomDao;
import chess.web.dto.MoveDto;
import chess.web.dto.MoveResultDto;
import chess.web.dto.PlayResultDto;
import chess.web.service.fakedao.FakeChessBoardDao;
import chess.web.service.fakedao.FakePlayerDao;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ChessGameServiceTest {

    ChessBoardDao chessBoardDao = new FakeChessBoardDao();
    RoomDao playerDao = new FakePlayerDao();
    ChessGameService chessGameService = new ChessGameService(chessBoardDao, playerDao);

    @Test
    void start() {
        ChessGame chessGame = chessGameService.start();

        assertThat(chessGame.getBoard()).isNotNull();
    }

    @Test
    void play() {
        PlayResultDto playResultDto = chessGameService.play();

        Map<String, Piece> board = playResultDto.getBoard();
        String turn = playResultDto.getTurn();

        assertThat(board).isNotNull();
        assertThat(turn).isNotNull();
    }

    @Test
    void move() {
        MoveResultDto moveResultDto = chessGameService.move(new MoveDto("a2", "a4"));

        boolean isGameOver = moveResultDto.getIsGameOver();
        boolean isMovable = moveResultDto.getIsMovable();

        assertThat(isGameOver).isNotNull();
        assertThat(isMovable).isNotNull();
    }
}
