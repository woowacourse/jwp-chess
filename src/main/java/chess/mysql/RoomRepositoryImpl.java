package chess.mysql;

import chess.chessgame.domain.room.Room;
import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.game.ChessGameManagerFactory;
import chess.chessgame.domain.room.user.User;
import chess.chessgame.repository.RoomRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static chess.chessgame.domain.room.game.board.piece.attribute.Color.BLACK;
import static chess.chessgame.domain.room.game.board.piece.attribute.Color.WHITE;
import static java.util.stream.Collectors.toList;

@Repository
public class RoomRepositoryImpl implements RoomRepository {
    private static final int EMPTY_ID = 0;

    private final RoomDao roomDao;
    private final UserDao userDao;
    private final JdbcTemplateChessDao chessDao;

    public RoomRepositoryImpl(RoomDao roomDao, UserDao userDao, JdbcTemplateChessDao chessDao) {
        this.roomDao = roomDao;
        this.userDao = userDao;
        this.chessDao = chessDao;
    }

    @Transactional
    @Override
    public Room createRoom(String roomName, ChessGameManager chessGameManager, User whiteUser) {
        RoomDto roomDto = roomDao.insertRoom(new Room(EMPTY_ID, roomName, chessGameManager, whiteUser));
        userDao.updateRoomId(whiteUser.getUserId(), roomDto.getRoomId());
        return createRoomFrom(roomDto);
    }

    @Override
    public void updateBlackUser(Room room) {
        roomDao.updateBlackUser(room);
    }

    @Override
    public Room findRoomBy(long roomId) {
        RoomDto roomDto = roomDao.findByRoomId(roomId);
        return createRoomFrom(roomDto);
    }

    @Override
    public Room findRoomByUserId(long userId) {
        RoomDto roomDto = roomDao.findByUserId(userId);
        return createRoomFrom(roomDto);
    }

    @Override
    public List<Room> findAllActiveRoom() {
        return roomDao.findAllActiveRoom().stream()
                .map(this::createRoomFrom)
                .collect(toList());
    }

    private Room createRoomFrom(RoomDto roomDto) {
        ArrayList<User> users = new ArrayList<>();

        User whiteUser = userDao.findByRoomIdAndColor(roomDto.getRoomId(), WHITE)
                .orElseThrow(() -> new IllegalStateException("흰색 유저가 없습니다."));
        users.add(whiteUser);

        Optional<User> optionalBlackUser = userDao.findByRoomIdAndColor(roomDto.getRoomId(), BLACK);
        optionalBlackUser.ifPresent(users::add);

        ChessGameManager chessGame = chessDao.findById(roomDto.getGameId()).map(ChessGameManagerFactory::loadingGame)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게임이 없습니다"));

        return new Room(roomDto.getRoomId(), roomDto.getRoomName(), chessGame, users);
    }
}
