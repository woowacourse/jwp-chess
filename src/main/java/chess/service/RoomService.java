package chess.service;

import chess.chessgame.domain.room.Room;
import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.user.User;
import chess.chessgame.repository.ChessGameManagerRepository;
import chess.chessgame.repository.RoomRepository;
import chess.chessgame.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Room room = roomRepository.createRoom(roomName, chessGameManagerRepository.create(), whiteUser);
        userRepository.updateRoomId(whiteUser, room.getRoomId());
        return room;
    }

    public boolean isAuthorityUser(User user, long roomId) {
        return user.getRoomId() != roomId;
    }

    public List<Room> findAllRunningRoom() {
        return roomRepository.findAllActiveRoom();
    }

    public ChessGameManager findGameBy(long roomId) {
        return roomRepository.findRoomBy(roomId).getGameManager();
    }

    @Transactional
    public User findUserIfAbsentCreate(long roomId, String password) {
        Room room = roomRepository.findRoomBy(roomId);
        if (room.isMaxUser() || room.isUserExistsIn(WHITE, password)) {
            return userRepository.matchPasswordUser(roomId, password);
        }
        User blackUser = userRepository.createUser(BLACK, password);

        room.enterUser(blackUser);
        return userRepository.updateRoomId(blackUser, roomId);
    }
}
