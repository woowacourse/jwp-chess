package chess.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chess.dao.GameDao;
import chess.domain.piece.PieceColor;
import chess.dto.response.ChessGameDto;
import chess.dto.response.RoomDto;

public class GameDaoFake implements GameDao {
    private final Map<Integer, PieceColor> fakeGame = new HashMap<>();

    @Override
    public ChessGameDto getGame(int gameId) {
        PieceColor pieceColor = fakeGame.get(gameId);
        return ChessGameDto.of(gameId, pieceColor.name());
    }

    @Override
    public int createGame(String gameName, String gamePassword) {
        fakeGame.put(fakeGame.size() + 1, PieceColor.WHITE);
        return fakeGame.size();
    }

    @Override
    public void deleteGame(int gameId) {
        fakeGame.remove(gameId);
    }

    @Override
    public void updateTurnToWhite(int gameId) {
        fakeGame.put(gameId, PieceColor.WHITE);
    }

    @Override
    public void updateTurnToBlack(int gameId) {
        fakeGame.put(gameId, PieceColor.BLACK);
    }

    @Override
    public List<RoomDto> inquireAllRooms() {
        return null;
    }

    @Override
    public String getPasswordById(int gameId) {
        return null;
    }

    @Override
    public String toString() {
        return "GameDaoFake{" +
            "fakeGame=" + fakeGame +
            '}';
    }
}
