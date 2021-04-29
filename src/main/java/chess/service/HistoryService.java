package chess.service;

import chess.dao.HistoryDAO;
import chess.domain.ChessGame;
import org.springframework.stereotype.Service;

@Service
public final class HistoryService {
    private final HistoryDAO historyDAO;

    public HistoryService(final HistoryDAO historyDAO) {
        this.historyDAO = historyDAO;
    }

    public void createHistory(final String roomId, final String startPoint, final String endPoint) {
        historyDAO.createHistory(roomId, startPoint, endPoint);
    }

    public void continueGame(final String id, final ChessGame chessGame) {
        historyDAO.allHistoryByRoomId(id).forEach(
                positions -> chessGame.move(
                        positions.getStartPosition(),
                        positions.getEndPosition()
                )
        );
    }
}
