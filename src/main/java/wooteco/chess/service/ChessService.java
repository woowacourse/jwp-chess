package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.controller.command.Command;
import wooteco.chess.domain.ChessManager;
import wooteco.chess.dto.Commands;
import wooteco.chess.dto.GameResponse;
import wooteco.chess.repository.ChessRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChessService {
    private static final String MOVE_ERROR_MESSAGE = "이동할 수 없는 곳입니다. 다시 입력해주세요";

    //    private ChessDao chessDao;
    private ChessManager chessManager = new ChessManager();

    private ChessRepository chessRepository;

    public ChessService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

//    public ChessService(ChessDao chessDao) {
//        this.chessDao = chessDao;
//    }

    public void start() {
        chessManager.start();
    }

    public void playNewGame() {
        initializeDatabase();
    }

    public void playLastGame() {
//        List<CommandDto> commands = chessDao.selectCommands();
//        for (CommandDto command : commands) {
//            Command.MOVE.apply(chessManager, command.get());
//        }
    }

    public void move(String source, String target) {
        String command = String.join(" ", new String[]{"move", source, target});

        try {
            Command.MOVE.apply(chessManager, command);
            saveToDatabase(command);
        } catch (Exception e) {
            throw new IllegalArgumentException(MOVE_ERROR_MESSAGE);
        }

        if (!chessManager.isPlaying()) {
            initializeDatabase();
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
        if (!chessRepository.findAll().isEmpty()) {
            model.put("haveLastGameRecord", "true");
        }
//        if (!chessDao.selectCommands().isEmpty()) {
//            model.put("haveLastGameRecord", "true");
//        }

        return model;
    }

    public Map<String, Object> makeMoveResponse() {
        GameResponse gameResponse = new GameResponse(chessManager);
        Map<String, Object> model = new HashMap<>();
        model.put("chessPieces", gameResponse.getTiles());
        model.put("currentTeam", gameResponse.getCurrentTeam());
        model.put("currentTeamScore", gameResponse.getCurrentTeamScore());
        chessManager.getWinner().ifPresent(winner -> model.put("winner", winner));

        return model;
    }

    private void initializeDatabase() {
//        chessDao.clearCommands();
        chessRepository.deleteAll();
    }

    private void saveToDatabase(String command) {
//        chessDao.addCommand(new CommandDto(command));
        chessRepository.save(new Commands(command));
    }
}