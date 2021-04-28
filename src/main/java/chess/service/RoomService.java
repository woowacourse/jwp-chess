package chess.service;

import chess.chessgame.domain.room.Room;
import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.user.User;
import chess.chessgame.repository.ChessGameManagerRepository;
import chess.chessgame.repository.RoomRepository;
import chess.chessgame.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static chess.chessgame.domain.room.game.board.piece.attribute.Color.BLACK;
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

    @Transactional
    public Room createRoom(String roomName, String userPassword) {
        User whiteUser = userRepository.createUser(WHITE, userPassword);
        return roomRepository.createRoom(roomName, chessGameManagerRepository.create(), Arrays.asList(whiteUser));
    }

    public List<Room> findAllRunningRoom() {
        return roomRepository.findAllActiveRoom();
    }

    public ChessGameManager findGameBy(long roomId) {
        return roomRepository.findRoomBy(roomId).getGameManager();
    }

    public Room findRoomByUserId(long userId) {
        return roomRepository.findRoomByUserId(userId);
    }

    @Transactional
    public User findUserBy(long roomId, String password) {
        Room room = roomRepository.findRoomBy(roomId);
        if (room.isMaxUser() || room.isUserExistsIn(WHITE, password)) {
            return userRepository.matchPasswordUser(roomId, password);
        }
        User blackUser = userRepository.createUser(BLACK, password);
        room.enterUser(blackUser);
        roomRepository.updateBlackUser(room);

        return blackUser;
    }
}
