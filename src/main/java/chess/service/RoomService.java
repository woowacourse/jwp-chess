package chess.service;

import chess.model.game.ChessGame;
import chess.model.room.Room;
import chess.repository.ChessGameRepository;
import chess.repository.RoomRepository;
import chess.repository.dao.GameDao;
import chess.service.dto.response.DeleteGameResponse;
import chess.service.dto.response.RoomsDto;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = {DataAccessException.class, IllegalArgumentException.class})
public class RoomService {
    private final GameDao gameDao;
    private final ChessGameRepository chessGameRepository;
    private final RoomRepository roomRepository;

    public RoomService(GameDao gameDao, ChessGameRepository chessGameRepository, RoomRepository roomRepository) {
        this.gameDao = gameDao;
        this.chessGameRepository = chessGameRepository;
        this.roomRepository = roomRepository;
    }

    public RoomsDto getAllRooms() {
        return new RoomsDto(gameDao.findAll());
    }

    public void createRoom(String name, String password) {
        roomRepository.createRoom(Room.fromPlainPassword(name, password));
    }

    public DeleteGameResponse deleteRoom(Integer gameId, String plainPassword) {
        checkPassword(gameId, plainPassword);
        checkGamePlaying(gameId);
        roomRepository.deleteRoom(gameId);
        return new DeleteGameResponse(gameId, true);
    }

    private void checkPassword(Integer gameId, String plainPassword) {
        Room room = roomRepository.findById(gameId);
        if (!room.isSamePassword(plainPassword)) {
            throw new IllegalArgumentException("암호가 다릅니다.");
        }
    }

    private void checkGamePlaying(Integer gameId) {
        ChessGame game = chessGameRepository.findById(gameId);
        if (game.isPlaying()) {
            throw new IllegalArgumentException("게임 실행중에는 삭제할 수 없습니다.");
        }
    }
}
