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
        System.out.println("HERE1 생성자 ");
        List<ChessRoom> chessRooms = chessRoomRepository.findAll();
        for (ChessRoom chessRoom : chessRooms) {
            chessGames.put(chessRoom.getRoomId(), new ChessManager());
        }

        for (Map.Entry<Long, ChessManager> entry : chessGames.entrySet()) {
            System.out.println("생성됨" + entry.getKey() + ": " + entry.getValue());
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
        ChessManager chessManager = chessGames.get(roomId);
        System.out.println("HERE1 " + chessManager + " " + source + " " + target);
        chessManager.move(source, target);
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

    private void saveCommand(String source, String target, Long roomId) {
        String command = String.join(MOVE_DELIMITER, new String[]{"move", source, target});
        ChessRoom chessRoom = findRoom(roomId);
        chessRoom.addCommand(new MoveCommand(command));
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