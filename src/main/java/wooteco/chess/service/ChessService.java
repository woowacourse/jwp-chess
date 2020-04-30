package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.controller.command.Command;
import wooteco.chess.domain.ChessManager;
import wooteco.chess.dto.GameResponse;
import wooteco.chess.repository.ChessRoom;
import wooteco.chess.repository.ChessRoomRepository;
import wooteco.chess.repository.MoveCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChessService {
    private static final String MOVE_DELIMITER = " ";

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

    public Map<String, Object> playLastGame(Long roomId) throws IllegalArgumentException {
        List<MoveCommand> commands = findRoom(roomId).getMoveCommand();
        ChessManager chessManager = chessGames.get(roomId);
        chessManager.start();
        for (MoveCommand command : commands) {
            Command.MOVE.apply(chessManager, command.getCommand());
        }
        return makeMoveResponse(roomId);
    }

    public void move(String source, String target, Long roomId) {
        String command = String.join(MOVE_DELIMITER, new String[]{"move", source, target});
        ChessManager chessManager = chessGames.get(roomId);
        Command.MOVE.apply(chessManager, command);
        saveCommand(command, roomId);

        if (!chessManager.isPlaying()) {
            initializeCommandsById(roomId);
            chessManager.end();
        }
    }

    public void end(Long roomId) {
        ChessManager chessManager = chessGames.get(roomId);
        chessManager.end();
    }

    //TODO: private으로 변경
    public Map<String, Object> makeStartResponse() {
        Map<String, Object> model = new HashMap<>();
        model.put("chessRooms", chessRoomRepository.findAll());

        return model;
    }

    public Map<String, Object> makeMoveResponse(Long roomId) {
        ChessManager chessManager = chessGames.get(roomId);
        Map<String, Object> model = new HashMap<>(new GameResponse(chessManager).get());
        model.put("winner", chessManager.getWinner());
        model.put("roomId", roomId);
        model.put("roomName", findRoom(roomId).getRoomName());

        return model;
    }

    private void initializeCommandsById(Long roomId) {
        chessRoomRepository.deleteById(roomId);
    }

    private void saveCommand(String command, Long roomId) {
        ChessRoom chessRoom = findRoom(roomId);
        chessRoom.addCommand(new MoveCommand(command));
        ChessRoom savedRoom = chessRoomRepository.save(chessRoom);
    }

    private ChessRoom findRoom(Long roomId) {
        return chessRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
    }
}