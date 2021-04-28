package chess.service;

import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.PieceFactory;
import chess.domain.room.Room;
import chess.repository.RoomRepositoryImpl;
import chess.web.dto.RoomDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepositoryImpl roomRepository;

    @Test
    void getAllRooms() {
        when(roomRepository.allRooms())
                .thenReturn(Arrays.asList(
                        new Room(1L, "dummy", new ChessGame(new Board(PieceFactory.createPieces()))),
                        new Room(2L, "dummy", new ChessGame(new Board(PieceFactory.createPieces())))
                ));

        List<RoomDto> allRooms = roomService.getAllRooms();
        assertThat(allRooms).hasSize(2);
    }

    @Test
    void createNewRoom() {
        when(roomRepository.save(any())).thenReturn(1L);

        RoomDto dummy = roomService.createNewRoom("dummy");
        assertThat(dummy.getRoomId()).isEqualTo(1L);
    }
}