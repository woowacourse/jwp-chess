package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.command.Commands;
import chess.domain.utils.PieceInitializer;
import chess.dto.CommandDto;
import chess.dto.GameInfoDto;
import chess.dto.HistoryDto;
import chess.repository.CommandRepository;
import chess.repository.HistoryRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import spark.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChessService {
    private final CommandRepository commandRepository;
    private final HistoryRepository historyRepository;

    public ChessService(CommandRepository commandRepository, HistoryRepository historyRepository) {
        this.commandRepository = commandRepository;
        this.historyRepository = historyRepository;
    }

    public List<HistoryDto> loadHistory() {
        return histories();
    }

    public GameInfoDto initialGameInfo() {
        return new GameInfoDto(new ChessGame(Board.of(PieceInitializer.pieceInfo())));
    }

    public GameInfoDto continuedGameInfo(String id) {
        ChessGame chessGame = gameStateOf(id);
        if (chessGame.isEnd()) {
            updateDB(id);
        }
        return new GameInfoDto(chessGame);
    }

    private ChessGame gameStateOf(String id) {
        ChessGame chessGame = new ChessGame(Board.of(PieceInitializer.pieceInfo()));
        chessGame.makeBoardStateOf(lastState(id));
        return chessGame;
    }

    public void move(String id, String command, Commands commands) {
        ChessGame chessGame = gameStateOf(id);
        chessGame.moveAs(commands);
        updateMoveInfo(command, id);
    }

    private List<HistoryDto> histories() {
        return new ArrayList<>(historyRepository.selectActive());
    }

    public String addHistory(String name) {
        final int id = historyRepository.insert(name);
        return String.valueOf(id);
    }

    private void updateMoveInfo(String command, String historyId) {
        if (StringUtils.isNotEmpty(historyId)) {
            flushCommands(command, historyId);
        }
    }

    private void updateDB(String historyId) {
        historyRepository.updateEndState(historyId);
    }

    private void flushCommands(String command, String gameId) {
        try {
            commandRepository.insert(new CommandDto(command), Integer.parseInt(gameId));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<CommandDto> lastState(String id) {
        return commandRepository.selectAllCommands(id);
    }
}
