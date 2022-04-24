package chess.service;

import chess.dao.GameDao;
import chess.domain.game.GameId;
import chess.domain.piece.PieceColor;
import java.util.HashMap;
import java.util.Map;

public class GameDaoFake implements GameDao {
    private final Map<GameId, PieceColor> fakeGame = new HashMap<>();

    @Override
    public void createGame(GameId gameId) {
        fakeGame.put(gameId, PieceColor.WHITE);
    }

    @Override
    public void deleteGame(GameId gameId) {
        fakeGame.remove(gameId);
    }

    @Override
    public void updateTurnToWhite(GameId gameId) {
        fakeGame.put(gameId, PieceColor.WHITE);
    }

    @Override
    public void updateTurnToBlack(GameId gameId) {
        fakeGame.put(gameId, PieceColor.BLACK);
    }

    @Override
    public PieceColor getCurrentTurn(GameId gameId) {
        return fakeGame.get(gameId);
    }

    @Override
    public String toString() {
        return "GameDaoFake{" +
                "fakeGame=" + fakeGame +
                '}';
    }
}
