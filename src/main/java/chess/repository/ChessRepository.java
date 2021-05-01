package chess.repository;

import chess.domain.game.ChessGame;
import java.util.List;
import java.util.Optional;

public interface ChessRepository {

    Optional<Long> findGame(String title);

    Long addGame(ChessGame chessGame, String title);

    ChessGame loadGame(Long id);

    void saveGame(Long id, ChessGame chessGame);

    void finish(Long id);

    void restart(Long id, ChessGame chessGame);

    List<ChessGame> findAllGames();

    void deleteAll();
}
