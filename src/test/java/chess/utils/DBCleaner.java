package chess.utils;

import chess.dao.game.ChessGameRepository;
import chess.dao.player.PlayerRepository;
import chess.dao.playerpieceposition.PlayerPiecePositionRepository;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

@Component
public class DBCleaner {
    private final ChessGameRepository chessGameRepository;
    private final PlayerRepository playerRepository;
    private final PlayerPiecePositionRepository playerPiecePositionRepository;

    public DBCleaner(ChessGameRepository chessGameRepository, PlayerRepository playerRepository, PlayerPiecePositionRepository playerPiecePositionRepository) {
        this.chessGameRepository = chessGameRepository;
        this.playerRepository = playerRepository;
        this.playerPiecePositionRepository = playerPiecePositionRepository;
    }

    public void removeAll() throws SQLException {
        playerPiecePositionRepository.removeAll();
        playerRepository.removeAll();
        chessGameRepository.removeAll();
    }
}
