package chess.repository;

import chess.domain.Color;
import chess.web.dao.BoardRepository;
import chess.web.dto.GameStateDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeBoardRepository implements BoardRepository {

    private final Map<Integer, BoardData> database = new HashMap<>();
    private int autoIncrementId = 0;

    @Override
    public int save(int userId, GameStateDto gameStateDto) {
        autoIncrementId++;
        database.put(autoIncrementId, new BoardData(userId, gameStateDto.getTurn()));
        return autoIncrementId;
    }

    @Override
    public Color getTurn(int boardId) {
        return Color.valueOf(database.get(boardId).getTurn());
    }

    @Override
    public int getBoardIdByPlayer(int playerId) {
        return findBoardId(playerId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Optional<Integer> findBoardId(int playerId) {
        return database.keySet().stream()
                .filter(key -> database.get(key).getPlayerId() == playerId)
                .findAny();
    }

    @Override
    public void update(int boardId, GameStateDto gameStateDto) {
        BoardData board = database.get(boardId);
        database.put(boardId, new BoardData(board.getPlayerId(), gameStateDto.getTurn()));
    }

    @Override
    public void deleteByPlayer(int playerId) {
        findBoardId(playerId).ifPresent(database::remove);
    }

    private static class BoardData {
        private int playerId;
        private String turn;

        private BoardData(int playerId, String turn) {
            this.playerId = playerId;
            this.turn = turn;
        }

        public int getPlayerId() {
            return playerId;
        }

        public String getTurn() {
            return turn;
        }
    }
}
