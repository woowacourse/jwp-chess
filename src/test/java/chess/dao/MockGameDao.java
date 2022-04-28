package chess.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import chess.dao.game.GameDao;
import chess.domain.ChessGame;

public class MockGameDao implements GameDao {

    private final static Map<Long, ChessGame> store = new ConcurrentHashMap<>();
    private static int nextId = 1;

    @Override
    public Long save(ChessGame game) {
        game = new ChessGame(game.getBoard(), null, null, game.getTurn(), game.getParticipant());
        long id = nextId++;
        store.put(id, game);
        return id;
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
    public void move(final Long gameId, final ChessGame game, final String rawFrom, final String rawTo) {
        store.put(gameId, game);
    }

    @Override
    public void terminate(final Long id) {
        final ChessGame game = store.get(id);
        game.terminate();
        store.put(id, game);
    }
}
