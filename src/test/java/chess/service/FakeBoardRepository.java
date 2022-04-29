package chess.service;

import chess.domain.Color;
import chess.repository.BoardRepository;
import chess.web.dto.GameStateDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeBoardRepository implements BoardRepository {

    private final Map<Integer, BoardData> database = new HashMap<>();
    private int autoIncrementId = 0;

    @Override
    public int save(int roomId, GameStateDto gameStateDto) {
        autoIncrementId++;
        database.put(autoIncrementId, new BoardData(roomId, gameStateDto.getTurn(), gameStateDto.getEnd()));
        return autoIncrementId;
    }

    @Override
    public Color getTurn(int boardId) {
        return Color.valueOf(database.get(boardId).getTurn());
    }

    @Override
    public boolean getEnd(int boardId) {
        return database.get(boardId).getEnd();
    }

    @Override
    public Optional<Integer> findBoardIdByRoom(int roomId) {
        return findBoardId(roomId);
    }

    private Optional<Integer> findBoardId(int roomId) {
        return database.keySet().stream()
                .filter(key -> database.get(key).getRoomId() == roomId)
                .findAny();
    }

    @Override
    public void updateState(int boardId, GameStateDto gameStateDto) {
        BoardData board = database.get(boardId);
        database.put(boardId, new BoardData(board.getRoomId(), gameStateDto.getTurn(), gameStateDto.getEnd()));
    }

    @Override
    public void deleteByRoom(int roomId) {
        findBoardId(roomId).ifPresent(database::remove);
    }

    private static class BoardData {
        private int roomId;
        private String turn;
        private boolean end;

        private BoardData(int roomId, String turn, boolean end) {
            this.roomId = roomId;
            this.turn = turn;
            this.end = end;
        }

        public int getRoomId() {
            return roomId;
        }

        public String getTurn() {
            return turn;
        }

        public boolean getEnd() {
            return end;
        }
    }
}
