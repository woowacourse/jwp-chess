package chess.dao;

import chess.dao.game.GameDao;
import chess.domain.ChessGame;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MockGameDao implements GameDao {

    private final static Map<Long, ChessGame> store = new ConcurrentHashMap<>();
    private static long nextId = 1L;

    @Override
    public Long save(ChessGame game) {
        game = new ChessGame(nextId++, game.getBoard(), game.getTurn(), game.getRoomInfo());
        store.put(game.getId(), game);
        return game.getId();
    }

    @Override
    public Optional<ChessGame> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ChessGame> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<ChessGame> findHistoriesByMemberId(Long memberId) {
        return store.values()
                .stream()
                .filter(ChessGame::isEnd)
                .filter(game -> Objects.equals(game.getBlackId(), memberId)
                        || Objects.equals(game.getWhiteId(), memberId))
                .collect(Collectors.toList());
    }

    @Override
    public void move(final ChessGame game, final String rawFrom, final String rawTo) {
        store.put(game.getId(), game);
    }

    @Override
    public void terminate(final Long id) {
        final ChessGame game = store.get(id);
        game.terminate();
        store.put(id, game);
    }

    @Override
    public Long deleteById(final Long gameId) {
        store.remove(gameId);
        return gameId;
    }

    public void deleteAll() {
        store.clear();
    }
}
