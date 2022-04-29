package chess.repository;

import chess.domain.board.ChessBoard;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

@Repository
public class SessionToChessRepository {

    private static final int SESSION_REMOVE_MINUTE = 1;
    private Map<HttpSession, ChessBoard> sessionToChessBoard = new ConcurrentHashMap<>();

    public void add(HttpSession session, ChessBoard chessBoard) {
        sessionToChessBoard.put(session, chessBoard);
    }


    public ChessBoard get(HttpSession session) {
        return sessionToChessBoard.get(session);
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void checkAndRemoveSessions() {
        for (HttpSession session : sessionToChessBoard.keySet()) {
            checkAndRemoveSession(session);
        }
    }

    private void checkAndRemoveSession(HttpSession session) {
        long currentTimeMillis = System.currentTimeMillis();
        long lastAccessedTime = session.getLastAccessedTime();

        long elapsedMinutes = Duration.ofMillis(currentTimeMillis - lastAccessedTime).toMinutes();

        if (SESSION_REMOVE_MINUTE == elapsedMinutes) {
            sessionToChessBoard.remove(session);
            System.out.println("remove it " + session);
        }
    }
}
