package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.command.Commands;
import chess.domain.dto.CommandDto;
import chess.domain.dto.GameInfoDto;
import chess.domain.dto.HistoryDto;
import chess.domain.exception.DataException;
import chess.domain.repository.CommandRepository;
import chess.domain.repository.HistoryRepository;
import chess.domain.utils.PieceInitializer;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import spark.utils.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChessService {
    private CommandRepository commandRepository;
    private HistoryRepository historyRepository;

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

    public void move(String id, String command, Commands commands) throws SQLException {
        ChessGame chessGame = gameStateOf(id);
        chessGame.moveAs(commands);
        updateMoveInfo(command, id);
    }

    private List<HistoryDto> histories() {
        return new ArrayList<>(historyRepository.selectActive());
    }

    public String addHistory(String name) {
        historyRepository.insert(name);
        final Optional<Integer> id = historyRepository.findIdByName(name);
        if (!id.isPresent()) {
            throw new DataException("[ERROR] id 값을 불러올 수 없습니다.");
        }
        return String.valueOf(id.get());
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
