package chess.service;

import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.PieceFactory;
import chess.domain.room.Room;
import chess.repository.RoomRepositoryImpl;
import chess.web.dto.GameDto;
import chess.web.dto.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChessServiceTest {

    @InjectMocks
    ChessService chessService;

    @Mock
    RoomRepositoryImpl roomRepository;

    @BeforeEach
    void setUp() {
        ChessGame chessGame = new ChessGame(1L, new Board(PieceFactory.createPieces()));
        chessGame.start();
        when(roomRepository.findByRoomId(1L))
               .thenReturn(new Room(1L, "dummy", chessGame));
    }

    @Test
    void end() {
        MessageDto end = chessService.end(1L);
        assertThat(end.getMessage()).isEqualTo("finished");
    }

    @Test
    void loadByGameId() {
        GameDto gameDto = chessService.loadByGameId(1L);
    }

    @Test
    void getStatus() {
    }

    @Test
    void move() {
    }
}