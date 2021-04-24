package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.command.Commands;
import chess.domain.dao.SparkCommandDao;
import chess.domain.dao.SparkHistoryDao;
import chess.domain.dto.CommandDto;
import chess.domain.dto.GameInfoDto;
import chess.domain.dto.HistoryDto;
import chess.domain.utils.PieceInitializer;
import org.springframework.stereotype.Service;
import spark.utils.StringUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SparkChessService {

    private SparkCommandDao sparkCommandDao;
    private SparkHistoryDao sparkHistoryDao;

    public SparkChessService(SparkCommandDao sparkCommandDao, SparkHistoryDao sparkHistoryDao) {
        this.sparkCommandDao = sparkCommandDao;
        this.sparkHistoryDao = sparkHistoryDao;
    }

    public GameInfoDto initialGameInfo() {
        return new GameInfoDto(new ChessGame(Board.of(PieceInitializer.pieceInfo())));
    }

    public GameInfoDto continuedGameInfo(String id) throws SQLException {
        ChessGame chessGame = gameStateOf(id);
        if (chessGame.isEnd()) {
            updateDB(id);
        }
        return new GameInfoDto(chessGame);
    }

    private ChessGame gameStateOf(String id) throws SQLException {
        ChessGame chessGame = new ChessGame(Board.of(PieceInitializer.pieceInfo()));
        chessGame.makeBoardStateOf(lastState(id));
        return chessGame;
    }

    public void move(String id, String command, Commands commands) throws SQLException {
        ChessGame chessGame = gameStateOf(id);
        chessGame.moveAs(commands);
        updateMoveInfo(command, id, chessGame.isEnd());
    }

    public List<HistoryDto> loadHistory() {
        return sparkHistoryDao.selectActive()
            .stream()
            .map(HistoryDto::new)
            .collect(Collectors.toList());
    }

    public String addHistory(String name) throws SQLException {
        final Optional<String> id = sparkHistoryDao.insert(name);
        if (!id.isPresent()) {
            throw new SQLException("[ERROR] id 값을 불러올 수 없습니다.");
        }
        return id.get();
    }

    public void updateMoveInfo(String command, String historyId, boolean isEnd) throws SQLException {
        if (!StringUtils.isEmpty(historyId)) {
            flushCommands(command, historyId);
        }
    }

    private void updateDB(String historyId) {
        sparkHistoryDao.updateEndState(historyId);
    }

    public void flushCommands(String command, String gameId) {
        try {
            sparkCommandDao.insert(new CommandDto(command), Integer.valueOf(gameId));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<CommandDto> lastState(String id) throws SQLException {
        return sparkCommandDao.selectAllCommands(id);
    }

    public String getIdByName(String name) throws SQLException {
        final Optional<Integer> id = sparkHistoryDao.findIdByName(name);
        if (!id.isPresent()) {
            throw new SQLException("[ERROR] 해당 이름의 사용자가 존재하지 않습니다.");
        }
        return String.valueOf(id.get());
    }
}
