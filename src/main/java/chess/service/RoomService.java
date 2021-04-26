package chess.service;

import chess.domain.RoomRepository;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.PieceFactory;
import chess.domain.room.Room;
import chess.web.dto.RoomDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<RoomDto> getAllRooms() {
        return roomRepository.allRooms().stream()
                .map(RoomDto::new)
                .collect(toList());
    }

    @Transactional
    public RoomDto createNewRoom(String roomName) {
        Long roomId = saveGameToDB(roomName);

        return new RoomDto(new Room(roomId, roomName, null));
    }

    private Long saveGameToDB(String roomName) {
        ChessGame chessGame = new ChessGame(
                null,
                new Board(PieceFactory.createPieces())
        );
        chessGame.start();

        return roomRepository.save(
                new Room(
                        null,
                        roomName,
                        chessGame
                )
        );
    }

}
