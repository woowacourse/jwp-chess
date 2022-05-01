package chess.dao;

import chess.domain.board.ChessBoard;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Repository;

@Repository
public class SessionToChessRepository {

    private Map<HttpSession, ChessBoard> sessionToChessBoard = new ConcurrentHashMap<>();

    public void add(HttpSession session, ChessBoard chessBoard) {
        sessionToChessBoard.put(session, chessBoard);
    }


    public ChessBoard get(HttpSession session) {
        return sessionToChessBoard.get(session);
    }

    public void delete(HttpSession session) {
        sessionToChessBoard.remove(session);
    }
}
