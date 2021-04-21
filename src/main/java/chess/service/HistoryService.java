package chess.service;

import chess.dao.HistoryDAO;
import chess.domain.ChessGame;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class HistoryService {
    private final HistoryDAO historyDAO;

    public HistoryService(final HistoryDAO historyDAO) {
        this.historyDAO = historyDAO;
    }

    public void initializeByRoomId(final String roomId) {
        historyDAO.deleteLogByRoomId(roomId);
    }

    public List<String[]> logByRoomId(final String roomId) {
        return historyDAO.allLogByRoomId(roomId);
    }

    public void executeLog(final List<String[]> logs, final ChessGame chessGame) {
        logs.forEach(positions -> chessGame.move(positions[0], positions[1]));
    }

    public void createLog(final String roomId, final String startPoint, final String endPoint) {
        historyDAO.createLog(roomId, startPoint, endPoint);
    }
}
