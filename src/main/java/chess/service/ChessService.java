package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.command.Commands;
import chess.domain.utils.PieceInitializer;
import chess.dto.CommandDto;
import chess.dto.GameInfoDto;
import chess.dto.RoomDto;
import chess.repository.CommandRepository;
import chess.repository.UserRepository;
import chess.repository.RoomRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import spark.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChessService {
    private final CommandRepository commandRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ChessService(CommandRepository commandRepository, RoomRepository roomRepository,
                        UserRepository userRepository) {
        this.commandRepository = commandRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public List<RoomDto> loadHistory() {
        return histories();
    }

    public GameInfoDto gameInfo(String id) {
        final List<CommandDto> commands = lastState(id);
        if (commands.isEmpty()) {
            return initialGameInfo();
        }
        return continuedGameInfo(id, commands);
    }

    public GameInfoDto initialGameInfo() {
        return new GameInfoDto(new ChessGame(Board.of(PieceInitializer.pieceInfo())));
    }

//    public GameInfoDto continuedGameInfo1(String id) {
//        ChessGame chessGame = gameStateOf(id);
//        if (chessGame.isEnd()) {
//            updateDB(id);
//        }
//        return new GameInfoDto(chessGame);
//    }

    public GameInfoDto continuedGameInfo(String id, List<CommandDto> commands) {
        ChessGame chessGame = restore(commands);
        if (chessGame.isEnd()) {
            updateDB(id);
        }
        return new GameInfoDto(chessGame);
    }

    private ChessGame restore(List<CommandDto> commands) {
        ChessGame chessGame = new ChessGame(Board.of(PieceInitializer.pieceInfo()));
        chessGame.makeBoardStateOf(commands);
        return chessGame;
    }

//    private ChessGame gameStateOf(String id) {
//        ChessGame chessGame = new ChessGame(Board.of(PieceInitializer.pieceInfo()));
//        chessGame.makeBoardStateOf(lastState(id));
//        return chessGame;
//    }

    private List<CommandDto> lastState(String id) {
        return commandRepository.selectAllCommandsByRoomId(id);
    }

    public void move(String id, String command) {
//        ChessGame chessGame = gameStateOf(id);
        ChessGame chessGame = restore(lastState(id));
        chessGame.moveAs(new Commands(command));
        updateMoveInfo(command, id);
    }

    private List<RoomDto> histories() {
        return new ArrayList<>(roomRepository.selectWaitRooms());
    }

    public String addRoom(String name) {
        final int id = roomRepository.insert(name);
        return String.valueOf(id);
    }

    private void updateMoveInfo(String command, String historyId) {
        if (StringUtils.isNotEmpty(historyId)) {
            flushCommands(command, historyId);
        }
    }

    private void updateDB(String historyId) {
        roomRepository.updateWaitState(historyId);
    }

    private void flushCommands(String command, String gameId) {
        try {
            commandRepository.insert(new CommandDto(command), Integer.parseInt(gameId));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUser(String roomId, String password) {
        userRepository.insert(roomId, password);
    }
}
