package chess.repository;

import chess.dao.ChessDAO;
import chess.domain.RoomRepository;
import chess.domain.game.ChessGame;
import chess.domain.room.Room;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
public class RoomRepositoryImpl implements RoomRepository {

    private final ChessDAO chessDAO;

    public RoomRepositoryImpl(ChessDAO chessDAO) {
        this.chessDAO = chessDAO;
    }

    @Override
    public Long save(Room room) {
        String roomName = room.getRoomName();
        ChessGame chessGame = room.getChessGame();

        Long gameId = chessDAO.addChessGame(
                ChessGameConvertor.chessGameToJson(chessGame)
        );

        return chessDAO.addRoom(
                roomName,
                gameId
        );
    }

    @Override
    public void update(Room room) {
        chessDAO.updateChessGame(
                room.getChessGame().getGameId(),
                ChessGameConvertor.chessGameToJson(room.getChessGame())
        );
    }

    @Override
    public Room findByRoomId(Long roomId) {
        ChessDAO.Room room = chessDAO.findRoomByRoomId(roomId);

        ChessGame chessGame = ChessGameConvertor.jsonToChessGame(roomId, room.getGameData());

        return new Room(roomId, room.getRoomName(), chessGame);
    }

    @Override
    public List<Room> allRooms() {
        List<ChessDAO.Room> rooms = chessDAO.allRooms();

        return rooms.stream()
                .map(room -> new Room(
                        room.getRoomId(),
                        room.getRoomName(),
                        ChessGameConvertor.jsonToChessGame(
                                room.getGameId(),
                                room.getGameData()
                        )
                ))
                .collect(toList());
    }
}
