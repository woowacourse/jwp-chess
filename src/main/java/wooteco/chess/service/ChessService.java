package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.controller.command.Command;
import wooteco.chess.dao.ChessDao;
import wooteco.chess.domain.ChessManager;
import wooteco.chess.dto.Commands;
import wooteco.chess.dto.GameResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChessService {
    private static final String MOVE_ERROR_MESSAGE = "이동할 수 없는 곳입니다. 다시 입력해주세요";
    private static final String MOVE_DELIMITER = " ";

    private ChessDao chessDao;
    private ChessManager chessManager;

    @Autowired(required = false)
    public ChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public void start() {
        chessManager = new ChessManager();
        chessManager.start();
    }

    public void playNewGame() {
        initializeDatabase();
    }

    public void playLastGame() {
        List<Commands> commands = chessDao.findAll();
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
        GameResponse gameResponse = new GameResponse(chessManager);
        Map<String, Object> model = new HashMap<>();
        model.put("chessPieces", gameResponse.getTiles());
        model.put("currentTeam", gameResponse.getCurrentTeam());
        model.put("currentTeamScore", gameResponse.getCurrentTeamScore());
        if (!chessDao.findAll().isEmpty()) {
            model.put("haveLastGameRecord", "true");
        }

        return model;
    }

    public Map<String, Object> makeMoveResponse() {
        GameResponse gameResponse = new GameResponse(chessManager);
        Map<String, Object> model = new HashMap<>();
        model.put("chessPieces", gameResponse.getTiles());
        model.put("currentTeam", gameResponse.getCurrentTeam());
        model.put("currentTeamScore", gameResponse.getCurrentTeamScore());

        if (chessManager.getWinner().isPresent()) {
            model.put("winner", chessManager.getWinner().get());
            chessManager.clearBoard();
        }
        return model;
    }

    private void initializeDatabase() {
        chessDao.deleteAll();
    }

    private void saveToDatabase(String command) {
        chessDao.save(new Commands(command));
    }
}