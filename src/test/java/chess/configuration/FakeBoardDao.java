package chess.configuration;

import chess.domain.Color;
import chess.repository.BoardDao;
import chess.domain.GameState;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeBoardDao implements BoardDao {

    private final Map<Integer, BoardData> database = new HashMap<>();
    private int autoIncrementId = 0;

    @Override
    public int save(int roomId, GameState gameState) {
        autoIncrementId++;
        database.put(autoIncrementId, new BoardData(roomId, gameState.getTurn()));
        return autoIncrementId;
    }

    @Override
    public Color getTurn(int boardId) {
        return Color.valueOf(database.get(boardId).getTurn());
    }

    @Override
    public int getBoardIdByRoom(int roomId) {
        return findBoardId(roomId)
            .orElseThrow(IllegalArgumentException::new);
    }

    private Optional<Integer> findBoardId(int roomId) {
        return database.keySet().stream()
            .filter(key -> database.get(key).getRoomId() == roomId)
            .findAny();
    }

    @Override
    public void updateTurn(int boardId, GameState gameState) {
        BoardData board = database.get(boardId);
        database.put(boardId, new BoardData(board.getRoomId(), gameState.getTurn()));
    }

    @Override
    public void deleteByRoom(int roomId) {
        findBoardId(roomId).ifPresent(database::remove);
    }

    private static class BoardData {
        private int roomId;
        private String turn;

        private BoardData(int roomId, String turn) {
            this.roomId = roomId;
            this.turn = turn;
        }

        public int getRoomId() {
            return roomId;
        }

        public String getTurn() {
            return turn.toUpperCase();
        }
    }
}
