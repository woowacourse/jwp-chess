package chess.service;

import chess.dao.GameDao;
import chess.domain.game.GameId;
import chess.domain.piece.PieceColor;
import chess.dto.response.ChessGameDto;
import java.util.HashMap;
import java.util.Map;

public class GameDaoFake implements GameDao {
    private final Map<GameId, PieceColor> fakeGame = new HashMap<>();

    @Override
    public ChessGameDto getGame(GameId gameId) {
        PieceColor pieceColor = fakeGame.get(gameId);
        return ChessGameDto.of(gameId, pieceColor.name());
    }

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
    public String toString() {
        return "GameDaoFake{" +
                "fakeGame=" + fakeGame +
                '}';
    }
}
