package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.controller.command.Command;
import wooteco.chess.domain.ChessManager;
import wooteco.chess.dto.GameResponse;
import wooteco.chess.repository.ChessRoom;
import wooteco.chess.repository.ChessRoomRepository;
import wooteco.chess.repository.MoveCommand;
import wooteco.chess.repository.MoveCommandRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChessService {
    private static final String MOVE_ERROR_MESSAGE = "이동할 수 없는 곳입니다. 다시 입력해주세요";
    private static final String MOVE_DELIMITER = " ";

    @Autowired
    private MoveCommandRepository moveCommandRepository;
    @Autowired
    private ChessRoomRepository chessRoomRepository;
    private ChessManager chessManager;

    public void start() {
        chessManager = new ChessManager();
        chessManager.start();
    }

    public void playNewGame(String roomName) {
        ChessRoom newRoom = chessRoomRepository.save(new ChessRoom(roomName));
    }

    public void playLastGame(Long roomId) throws IllegalArgumentException {
        ChessRoom savedRoom = chessRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
        List<MoveCommand> commands = savedRoom.getMoveCommand();

        for (MoveCommand command : commands) {
            Command.MOVE.apply(chessManager, command.getCommand());
        }
    }

    public void move(String source, String target, Long roomId) {
        String command = String.join(MOVE_DELIMITER, new String[]{"move", source, target});

        try {
            Command.MOVE.apply(chessManager, command);
            saveCommand(command, roomId);
        } catch (Exception e) {
            throw new IllegalArgumentException(MOVE_ERROR_MESSAGE);
        }

        if (!chessManager.isPlaying()) {
            initializeCommandsById(roomId);
            chessManager.end();
        }
    }

    public void end() {
        chessManager.end();
    }

    public Map<String, Object> makeStartResponse() {
        Map<String, Object> model = new HashMap<>(new GameResponse(chessManager).get());
        model.put("chessRooms", chessRoomRepository.findAll());

        return model;
    }

    public Map<String, Object> makeMoveResponse() {
        Map<String, Object> model = new HashMap<>(new GameResponse(chessManager).get());
        model.put("winner", chessManager.getWinner());

        return model;
    }

    private void initializeCommandsById(Long roomId) {
        chessRoomRepository.deleteById(roomId);
    }

    private void saveCommand(String command, Long roomId) {
        moveCommandRepository.save(new MoveCommand(command));
    }
}