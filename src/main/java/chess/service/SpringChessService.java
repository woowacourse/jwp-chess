package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.command.Commands;
import chess.domain.dto.CommandDto;
import chess.domain.dto.GameInfoDto;
import chess.domain.dto.HistoryDto;
import chess.domain.exception.DataException;
import chess.domain.repository.ChessGameRepository;
import chess.domain.utils.PieceInitializer;
import org.springframework.stereotype.Service;
import spark.utils.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpringChessService {
    private ChessGameRepository chessGameRepositoryImpl;

    public SpringChessService(ChessGameRepository chessGameRepositoryImpl) {
        this.chessGameRepositoryImpl = chessGameRepositoryImpl;
    }

    public List<HistoryDto> loadHistory() {
        return chessGameRepositoryImpl.selectActiveHistory()
            .stream()
            .map(HistoryDto::new)
            .collect(Collectors.toList());
    }

    public GameInfoDto initialGameInfo() {
        return new GameInfoDto(new ChessGame(Board.of(PieceInitializer.pieceInfo())));
    }

    public GameInfoDto continuedGameInfo(String id) {
        ChessGame chessGame = gameStateOf(id);
        if (chessGame.isEnd()) {
            chessGameRepositoryImpl.updateEndStateHistory(id);
        }
        return new GameInfoDto(chessGame);
    }

    public void move(String id, String command, Commands commands) {
        ChessGame chessGame = gameStateOf(id);
        chessGame.moveAs(commands);
        updateMoveInfo(command, id);
    }

    private ChessGame gameStateOf(String id) {
        ChessGame chessGame = new ChessGame(Board.of(PieceInitializer.pieceInfo()));
        chessGame.makeBoardStateOf(lastState(id));
        return chessGame;
    }

    private List<CommandDto> lastState(String id) {
        return chessGameRepositoryImpl.selectAllCommands(id);
    }

    private void updateMoveInfo(String command, String historyId) {
        if (!StringUtils.isEmpty(historyId)) {
            flushCommands(command, historyId);
        }
    }

    public String addHistory(String name) {
        chessGameRepositoryImpl.insertHistory(name);
        final Optional<Integer> id = chessGameRepositoryImpl.findHistoryIdByName(name);
        if (!id.isPresent()) {
            throw new DataException("[ERROR] id 값을 불러올 수 없습니다.");
        }
        return String.valueOf(id.get());
    }

    public void flushCommands(String command, String gameId) {
        chessGameRepositoryImpl.insertCommand(new CommandDto(command), Integer.parseInt(gameId));
    }

    public String getIdByName(String name) {
        final Optional<Integer> id = chessGameRepositoryImpl.findHistoryIdByName(name);
        if (!id.isPresent()) {
            throw new DataException("[ERROR] 해당 이름의 사용자가 존재하지 않습니다.");
        }
        return String.valueOf(id.get());
    }
}
