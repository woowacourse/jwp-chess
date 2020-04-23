package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dao.FakeHistoryDao;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.NormalStatus;
import wooteco.chess.domain.position.MovingPosition;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.ChessGameDto;
import wooteco.chess.dto.MovablePositionsDto;

import java.sql.SQLException;
import java.util.List;

@Service
public class SpringChessService {
    public void clearHistory() {
//        HistoryDao historyDao = new HistoryDao();
        FakeHistoryDao historyDao = new FakeHistoryDao();
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
        FakeHistoryDao historyDao = new FakeHistoryDao();
        return historyDao.selectMovingPositions();
    }

    public MovablePositionsDto findMovablePositions(String source) throws SQLException {
        ChessGame chessGame = new ChessGame();
        load(chessGame);

        List<String> movablePositionNames = chessGame.findMovablePositionNames(source);

        return new MovablePositionsDto(movablePositionNames, source);

    }
}
