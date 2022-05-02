package chess.dao;

import chess.domain.ChessGame;
import chess.domain.square.Square;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MockGameDao implements GameDao {

    private final static Map<Long, ChessGame> store = new ConcurrentHashMap<>();
    private static int nextId = 1;

    @Override
    public ChessGame save(ChessGame game) {
        game = new ChessGame((long) nextId++, game.getBoard(), game.getTurn(), game.getRoom());
        store.put(game.getId(), game);
        return game;
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
    public void terminate(ChessGame game) {
        game.terminate();
        store.put(game.getId(), game);
    }

    @Override
    public void updateByMove(final ChessGame game, final String rawFrom, final String rawTo) {
        final Square from = Square.from(rawFrom);
        final Square to = Square.from(rawTo);
        game.move(from, to);
        store.put(game.getId(), game);
    }

    @Override
    public void deleteGameById(final Long id) {
        store.remove(id);
    }
}
