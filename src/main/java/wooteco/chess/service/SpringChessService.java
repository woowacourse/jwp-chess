package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dao.FakeHistoryDao;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.NormalStatus;
import wooteco.chess.domain.position.MovingPosition;
import wooteco.chess.dto.*;

import java.sql.SQLException;
import java.util.List;

@Service
public class SpringChessService {
    FakeHistoryDao fakeHistoryDao = new FakeHistoryDao();

    public void clearHistory() {
        fakeHistoryDao.clear();
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
        return fakeHistoryDao.selectMovingPositions();
    }

    public MovablePositionsDto findMovablePositions(String source) throws SQLException {
        ChessGame chessGame = new ChessGame();
        load(chessGame);

        List<String> movablePositionNames = chessGame.findMovablePositionNames(source);

        return new MovablePositionsDto(movablePositionNames, source);

    }

    public DestinationPositionDto getDestinationPosition(String destination) {
        return new DestinationPositionDto(destination, NormalStatus.YES);
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
        fakeHistoryDao.insert(movingPosition);
    }
}
