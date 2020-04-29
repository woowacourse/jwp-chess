package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.controller.command.Command;
import wooteco.chess.domain.ChessManager;
import wooteco.chess.repository.ChessRoomRepository;
import wooteco.chess.repository.Commands;
import wooteco.chess.dto.GameResponse;
import wooteco.chess.repository.CommandsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChessService {
    private static final String MOVE_ERROR_MESSAGE = "이동할 수 없는 곳입니다. 다시 입력해주세요";
    private static final String MOVE_DELIMITER = " ";

    @Autowired
    private CommandsRepository commandsRepository;
    @Autowired
    private ChessRoomRepository chessRoomRepository;
    private ChessManager chessManager;

    public void start() {
        chessManager = new ChessManager();
        chessManager.start();
    }

    public void playNewGame() {
        initializeDatabase();
    }

    public void playLastGame(Long roomId) {
        List<Commands> commands = commandsRepository.findByRoomId(roomId);
        for (Commands command : commands) {
            Command.MOVE.apply(chessManager, command.get());
        }
    }

    public void move(String source, String target) {
        String command = String.join(MOVE_DELIMITER, new String[]{"move", source, target});

        try {
            Command.MOVE.apply(chessManager, command);
            saveToDatabase(command);
        } catch (Exception e) {
            throw new IllegalArgumentException(MOVE_ERROR_MESSAGE);
        }

        if (!chessManager.isPlaying()) {
            initializeDatabase();
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

    private void initializeDatabase() {
        commandsRepository.deleteAll();
    }

    private void saveToDatabase(String command) {
        commandsRepository.save(new Commands(command));
    }
}