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
    public int getBoardIdByroom(int roomId) {
        return findBoardId(roomId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Optional<Integer> findBoardId(int roomId) {
        return database.keySet().stream()
                .filter(key -> database.get(key).getroomId() == roomId)
                .findAny();
    }

    @Override
    public void update(int boardId, GameStateDto gameStateDto) {
        BoardData board = database.get(boardId);
        database.put(boardId, new BoardData(board.getroomId(), gameStateDto.getTurn()));
    }

    @Override
    public void deleteByroom(int roomId) {
        findBoardId(roomId).ifPresent(database::remove);
    }

    private static class BoardData {
        private int roomId;
        private String turn;

        private BoardData(int roomId, String turn) {
            this.roomId = roomId;
            this.turn = turn;
        }

        public int getroomId() {
            return roomId;
        }

        public String getTurn() {
            return turn;
        }
    }
}
