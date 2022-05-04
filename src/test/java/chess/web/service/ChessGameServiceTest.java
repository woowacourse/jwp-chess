package chess.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.piece.StartedPawn;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;
import chess.web.dao.ChessBoardDao;
import chess.web.dao.PlayerDao;
import chess.web.dao.RoomDao;
import chess.web.dto.MoveDto;
import chess.web.dto.PlayResultDto;
import chess.web.service.fakedao.FakeChessBoardDao;
import chess.web.service.fakedao.FakePlayerDao;
import chess.web.service.fakedao.FakeRoomDao;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ChessGameServiceTest {

    ChessBoardDao chessBoardDao = new FakeChessBoardDao();
    PlayerDao playerDao = new FakePlayerDao();
    RoomDao roomDao = new FakeRoomDao();
    ChessGameService chessGameService = new ChessGameService(chessBoardDao, playerDao, roomDao);

    @Test
    void start() {
        ChessGame chessGame = chessGameService.start(1);

        assertThat(chessGame.getBoard()).isNotNull();
    }

    @Test
    void play() {
        PlayResultDto playResultDto = chessGameService.play(1);

        Map<String, Piece> board = playResultDto.getBoard();
        String turn = playResultDto.getTurn();

        assertAll(() -> assertNotNull(board), () -> assertNotNull(turn));
    }

    @Test
    void move() {
        chessBoardDao.save(Position.of("a2"), new StartedPawn(Color.WHITE));
        PlayResultDto playResultDto = chessGameService.move(new MoveDto(1, "a2", "a4"));

        Map<String, Piece> board = playResultDto.getBoard();
        boolean isFinished = playResultDto.getIsFinished();

        assertThat(isFinished).isNotNull();
        assertThat(board).isNotNull();
    }
}
