package wooteco.chess.service;

import wooteco.chess.dao.DatabaseHistoryDao;
import wooteco.chess.dao.HistoryDao;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.NormalStatus;
import wooteco.chess.domain.position.MovingPosition;
import wooteco.chess.dto.*;

import java.sql.SQLException;
import java.util.List;

public class SparkChessService {
    public void clearHistory() throws SQLException {
        HistoryDao historyDao = new DatabaseHistoryDao();

        historyDao.clear();
    }

    public ChessGameDto setBoard() throws SQLException {
        ChessGame chessGame = new ChessGame();

        load(chessGame);

        return new ChessGameDto(new BoardDto(chessGame.getPieces()), chessGame.getTurn(), chessGame.calculateScore(), NormalStatus.YES.isNormalStatus());
    }

    private void load(ChessGame chessGame) throws SQLException {
        List<MovingPosition> histories = selectAllHistory();

        for (MovingPosition movingPosition : histories) {
            chessGame.move(movingPosition);
        }
    }

    private List<MovingPosition> selectAllHistory() throws SQLException {
        HistoryDao historyDao = new DatabaseHistoryDao();
        return historyDao.selectAll();
    }

    public MoveStatusDto move(MovingPosition movingPosition) throws SQLException {
        if (movingPosition.isStartAndEndSame()) {
            return new MoveStatusDto(NormalStatus.YES.isNormalStatus());
        }

        ChessGame chessGame = new ChessGame();

        load(chessGame);
        chessGame.move(movingPosition);

        if (chessGame.isKingDead()) {
            MoveStatusDto moveStatusDto = new MoveStatusDto(NormalStatus.YES.isNormalStatus(), chessGame.getAliveKingColor());
            clearHistory();
            return moveStatusDto;
        }

        insertHistory(movingPosition);

        return new MoveStatusDto(NormalStatus.YES.isNormalStatus());
    }

    private void insertHistory(MovingPosition movingPosition) throws SQLException {
        HistoryDao historyDao = new DatabaseHistoryDao();
        historyDao.insert(movingPosition);
    }

    public MovablePositionsDto findMovablePositions(String position) throws SQLException {
        ChessGame chessGame = new ChessGame();
        load(chessGame);

        List<String> movablePositionNames = chessGame.findMovablePositionNames(position);

        return new MovablePositionsDto(movablePositionNames, position);
    }

    public DestinationPositionDto chooseDestinationPosition(String position) {
        return new DestinationPositionDto(position, NormalStatus.YES);
    }
}
