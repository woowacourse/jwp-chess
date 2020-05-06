package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.domain.ChessManager;
import wooteco.chess.dto.response.MoveResponse;
import wooteco.chess.dto.response.StartResponse;
import wooteco.chess.repository.ChessRoom;
import wooteco.chess.repository.ChessRoomRepository;
import wooteco.chess.repository.MoveCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChessService {

    @Autowired
    private ChessRoomRepository chessRoomRepository;
    private Map<Long, ChessManager> chessGames = new HashMap<>();

    public void start() {
        List<ChessRoom> chessRooms = chessRoomRepository.findAll();
        for (ChessRoom chessRoom : chessRooms) {
            chessGames.put(chessRoom.getRoomId(), new ChessManager());
        }
    }

    public Long playNewGame(String roomName) {
        ChessRoom newRoom = chessRoomRepository.save(new ChessRoom(roomName));
        chessGames.put(newRoom.getRoomId(), new ChessManager());
        return newRoom.getRoomId();
    }

    public void playLastGame(Long roomId) throws IllegalArgumentException {
        List<MoveCommand> commands = findRoom(roomId).getMoveCommand();
        ChessManager chessManager = chessGames.get(roomId);
        chessManager.start();
        for (MoveCommand command : commands) {
            chessManager.move(command.getSource(), command.getTarget());
        }
    }

    public void move(String source, String target, Long roomId) {
        ChessManager chessManager = chessGames.get(roomId);
        try {
            chessManager.move(source, target);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        saveCommand(source, target, roomId);
    }

    public void checkIfPlaying(Long roomId) {
        ChessManager chessManager = chessGames.get(roomId);
        if (!chessManager.isPlaying()) {
            deleteRoom(roomId);
        }
    }

    public void end(Long roomId) {
        ChessManager chessManager = chessGames.get(roomId);
        chessManager.end();
    }

    public StartResponse getStartResponse() {
        return new StartResponse(chessRoomRepository.findAll());
    }

    public MoveResponse getMoveResponse(Long roomId) {
        ChessManager chessManager = chessGames.get(roomId);
        return new MoveResponse(chessManager, roomId, findRoom(roomId).getRoomName());
    }

    private void saveCommand(String source, String target, Long roomId) {
        ChessRoom chessRoom = findRoom(roomId);
        chessRoom.addCommand(new MoveCommand(source, target));
        chessRoomRepository.save(chessRoom);
    }

    private ChessRoom findRoom(Long roomId) {
        return chessRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
    }

    private void deleteRoom(Long roomId) {
        chessRoomRepository.deleteById(roomId);
    }
}