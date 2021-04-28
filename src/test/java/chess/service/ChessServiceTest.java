package chess.service;

import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.PieceFactory;
import chess.domain.room.Room;
import chess.repository.RoomRepositoryImpl;
import chess.web.dto.GameDto;
import chess.web.dto.MessageDto;
import chess.web.dto.PieceDto;
import chess.web.dto.StatusDto;
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
        //given
        MessageDto end = chessService.end(1L);

        //then
        assertThat(end.getMessage()).isEqualTo("finished");
    }

    @Test
    void loadByGameId() {
        //given
        GameDto gameDto = chessService.loadByGameId(1L);

        //then
        assertThat(gameDto.getTurn()).isEqualTo("white");
    }

    @Test
    void getStatus() {
        //given
        StatusDto status = chessService.getStatus(1L);

        //then
        assertThat(status.getBlackScore()).isEqualTo(34.0);
        assertThat(status.getWhiteScore()).isEqualTo(34.0);
    }

    @Test
    void move() {
        //given
        GameDto move = chessService.move(1L, "a2", "a3");

        PieceDto pieceDto = move.getPieceDtos().stream()
                .filter(piece -> piece.getNotation().equals("p"))
                .filter(piece -> piece.getPosition().equals("a3"))
                .findAny().orElse(null);

        assertThat(pieceDto).isNotNull();
    }
}