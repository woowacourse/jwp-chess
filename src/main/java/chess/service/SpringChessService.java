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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpringChessService {
    private CommandRepository commandRepository;
    private HistoryRepository historyRepository;

    public SpringChessService(CommandRepository commandRepository, HistoryRepository historyRepository) {
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
        return historyRepository.selectActive()
                .stream()
                .map(HistoryDto::new)
                .collect(Collectors.toList());
    }

    public String addHistory(String name) {
        historyRepository.insert(name);
        final Optional<Integer> id = historyRepository.findIdByName(name);
        if (!id.isPresent()) {
            throw new DataException("[ERROR] id 값을 불러올 수 없습니다.");
        }
        return String.valueOf(id.get());
    }

    public void updateMoveInfo(String command, String historyId) {
        if (!StringUtils.isEmpty(historyId)) {
            flushCommands(command, historyId);
        }
    }

    private void updateDB(String historyId) {
        historyRepository.updateEndState(historyId);
    }

    public void flushCommands(String command, String gameId) {
        try {
            commandRepository.insert(new CommandDto(command), Integer.parseInt(gameId));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<CommandDto> lastState(String id) {
        List<CommandDto> sample = commandRepository.selectAllCommands(id);
        for (CommandDto commandDto : sample) {
            System.out.println(commandDto.data());
        }
        return commandRepository.selectAllCommands(id);
    }

    public String getIdByName(String name) {
        final Optional<Integer> id = historyRepository.findIdByName(name);
        if (!id.isPresent()) {
            throw new DataException("[ERROR] 해당 이름의 사용자가 존재하지 않습니다.");
        }
        return String.valueOf(id.get());
    }
}
