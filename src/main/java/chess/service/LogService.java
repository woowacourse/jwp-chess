package chess.service;

import chess.dao.LogDAO;
import chess.domain.ChessGame;
import org.springframework.stereotype.Service;

@Service
public final class LogService {
    private final LogDAO logDAO;

    public LogService(final LogDAO logDAO) {
        this.logDAO = logDAO;
    }

    public void initializeByRoomId(final String roomId) {
        logDAO.deleteLogByRoomId(roomId);
    }

    public void createLog(final String roomId, final String startPoint, final String endPoint) {
        logDAO.createLog(roomId, startPoint, endPoint);
    }

    public void continueGame(final String id, final ChessGame chessGame) {
        logDAO.allLogByRoomId(id).forEach(
                positions -> chessGame.move(
                        positions.getStartPoint(),
                        positions.getEndPoint()
                )
        );
    }
}
