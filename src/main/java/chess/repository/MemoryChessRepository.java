package chess.repository;

import chess.domain.game.ChessGame;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("test")
public class MemoryChessRepository implements ChessRepository {

    private final Map<Long, ChessGame> store = new LinkedHashMap<>();
    private Long id = 0L;

    @Override
    public Optional<Long> findGame(String title) {
        return store.values().stream()
            .filter(chessGame -> chessGame.getTitle().equals(title))
            .map(ChessGame::getId)
            .findAny();
    }

    @Override
    public Long addGame(ChessGame chessGame, String title) {
        ++id;
        store.put(id, new ChessGame(id, title));
        return id;
    }

    @Override
    public ChessGame loadGame(Long id) {
        return store.get(id);
    }

    @Override
    public void saveGame(Long id, ChessGame chessGame) {
        store.put(id, chessGame);
    }

    @Override
    public void finish(Long id) {
        ChessGame chessGame = store.get(id);
        chessGame.finish();
    }

    @Override
    public void restart(Long id, ChessGame chessGame) {
        store.put(id, new ChessGame(id, chessGame.getTitle()));
    }

    @Override
    public List<ChessGame> findAllGames() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteAll() {
        store.clear();
    }
}
