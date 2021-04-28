package chess.service;

import chess.domain.RoomRepository;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.PieceFactory;
import chess.domain.room.Room;
import chess.web.dto.RoomDto;
import org.springframework.cache.annotation.Cacheable;
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

    //is cacheable?
    public List<RoomDto> getAllRooms() {
        return roomRepository.allRooms().stream()
                .map(RoomDto::new)
                .collect(toList());
    }

    public RoomDto createNewRoom(String roomName) {
        ChessGame chessGame = createChessGame();
        Long roomId = saveGameToDB(roomName, chessGame);

        return new RoomDto(new Room(roomId, roomName, chessGame));
    }

    private ChessGame createChessGame() {
        ChessGame chessGame = new ChessGame(
                new Board(PieceFactory.createPieces())
        );
        chessGame.start();

        return chessGame;
    }


    private Long saveGameToDB(String roomName, ChessGame chessGame) {
        return roomRepository.save(
                new Room(
                        roomName,
                        chessGame
                )
        );
    }

}
