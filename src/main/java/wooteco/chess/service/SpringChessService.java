package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dao.FakeHistoryDao;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.NormalStatus;
import wooteco.chess.domain.position.MovingPosition;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.ChessGameDto;
import wooteco.chess.dto.MovablePositionsDto;
import wooteco.chess.dto.MoveStatusDto;

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

    public MoveStatusDto checkMovable(MovingPosition movingPosition) {
        try {
            ChessGame chessGame = new ChessGame();

            load(chessGame);
            chessGame.move(movingPosition);
            return new MoveStatusDto(NormalStatus.YES.isNormalStatus());
        } catch (IllegalArgumentException | UnsupportedOperationException | NullPointerException | SQLException e) {
            return new MoveStatusDto(NormalStatus.NO.isNormalStatus(), e.getMessage());
        }
    }

    public MoveStatusDto move(MovingPosition movingPosition) throws SQLException {
        ChessGame chessGame = new ChessGame();
        load(chessGame);
        chessGame.move(movingPosition);

        MoveStatusDto moveStatusDto = new MoveStatusDto(NormalStatus.YES.isNormalStatus());

        if (chessGame.isKingDead()) {
            moveStatusDto = new MoveStatusDto(NormalStatus.YES.isNormalStatus(), chessGame.getAliveKingColor());
        }

        insertHistory(movingPosition);

        return moveStatusDto;
    }

    private void insertHistory(MovingPosition movingPosition) throws SQLException {
        fakeHistoryDao.insert(movingPosition);
    }
}
