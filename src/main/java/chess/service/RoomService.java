package chess.service;

import chess.chessgame.domain.room.Room;
import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.user.User;
import chess.chessgame.repository.ChessGameManagerRepository;
import chess.chessgame.repository.RoomRepository;
import chess.chessgame.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static chess.chessgame.domain.room.game.board.piece.attribute.Color.WHITE;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final ChessGameManagerRepository chessGameManagerRepository;
    private final UserRepository userRepository;

    public RoomService(RoomRepository roomRepository, ChessGameManagerRepository chessGameManagerRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.chessGameManagerRepository = chessGameManagerRepository;
        this.userRepository = userRepository;
    }

    public Room createRoom(String roomName, String userPassword) {
        User whiteUser = userRepository.createUser(userPassword, WHITE);
        return roomRepository.createRoom(roomName, chessGameManagerRepository.create(), Arrays.asList(whiteUser));
    }

    public List<Room> findAllRunningRoom() {
        return roomRepository.findAllActiveRoom();
    }

    public ChessGameManager findGameBy(long roomId) {
        return roomRepository.findRoomBy(roomId).getGameManager();
    }
}
