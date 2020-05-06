package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.controller.command.Command;
import wooteco.chess.domain.ChessManager;
import wooteco.chess.domain.ChessRunner;
import wooteco.chess.dto.ChessRoom;
import wooteco.chess.dto.Commands;
import wooteco.chess.dto.GameResponse;
import wooteco.chess.repository.ChessRoomRepository;
import wooteco.chess.repository.CommandRepository;

import java.util.*;

@Service
public class ChessService {
    private static final String MOVE_ERROR_MESSAGE = "이동할 수 없는 곳입니다. 다시 입력해주세요";
    private static final String MOVE_DELIMITER = " ";
    private final CommandRepository commandRepository;
    private ChessRoomRepository chessRoomRepository;
    private ChessManager chessManager;

    public ChessService(CommandRepository commandRepository, ChessRoomRepository chessRoomRepository) {
        this.commandRepository = commandRepository;
        this.chessRoomRepository = chessRoomRepository;
    }

    public void playNewGame(String roomName) {
        long randomNumber = createChessRoomNumber();
        chessRoomRepository.saveRoom(randomNumber, roomName);
        chessManager = new ChessManager(randomNumber);
        chessManager.start();
    }

    private long createChessRoomNumber() {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        return random.nextInt(10000);
    }

    public void playLastGame(Long roomNumber) {
        List<Commands> allByRoomNumber = commandRepository.findAllByRoomNumber(roomNumber);

        chessManager = new ChessManager(roomNumber, new ChessRunner());
        if(allByRoomNumber.size() == 0) {
            chessManager.start();
            return;
        }

        for (Commands command : allByRoomNumber) {
            Command.MOVE.apply(chessManager, command.getCommand());
        }
    }

    public void move(Long roomNumber, String source, String target) {
        String command = String.join(MOVE_DELIMITER, new String[]{"move", source, target});

        try {
            Command.MOVE.apply(chessManager, command);
            saveToDatabase(roomNumber, command);
        } catch (Exception e) {
            throw new IllegalArgumentException(MOVE_ERROR_MESSAGE);
        }

        checkGameOver(roomNumber);
    }

    private void checkGameOver(Long roomNumber) {
        if (!chessManager.isPlaying()) {
            chessRoomRepository.deleteById(roomNumber);
            commandRepository.deleteById(roomNumber);
            chessManager.end();
        }
    }

    public void end() {
        chessManager.end();
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

    private void saveToDatabase(Long roomNumber, String command) {
        commandRepository.save(new Commands(roomNumber, command));
    }

    public Long getRoomNumber() {
        return chessManager.getId();
    }

    public List<ChessRoom> findAllRooms() {
        return chessRoomRepository.findAllChessRoom();
    }

    public String getRoomName(Long id) {
        return chessRoomRepository.findRoomNameById(id);
    }
}