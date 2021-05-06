package chess.repository;

import chess.domain.board.ChessBoardFactory;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.*;

public class InMemoryRepository implements ChessRepository {
    private int counter = 0;

    private final Map<Integer, Map<String, String>> room = new HashMap<>();
    private final Map<Integer, Map<String, String>> piece = new HashMap<>();

    @Override
    public int makeRoom(Map<String, String> board, String roomName) {
        room.put(++counter, new HashMap<String, String>() {{
            put(roomName, "white");
        }});
        piece.put(counter, board);
        return counter;
    }

    @Override
    public void initializePieceStatus(int roomId, Map<String, String> board) {
        piece.put(roomId, board);
    }

    @Override
    public Map<Position, Piece> getBoardByRoomId(int roomId) {
        Map<String, String> board = new LinkedHashMap<>();
        for (Map.Entry<String, String> cell : piece.get(roomId).entrySet()) {
            board.put(cell.getKey(), cell.getValue());
        }
        return ChessBoardFactory.createStoredBoard(board);
    }

    @Override
    public void moveSourcePieceToTargetPoint(String source, String target, int roomId) {
        Map<String, String> pieceInformation = piece.get(roomId);
        Object pieceName = pieceInformation.remove(source);
        pieceInformation.put(target, pieceName.toString());
    }

    @Override
    public void changeTurn(String nextTurn, String currentTurn, int roomId) {
        Map<String, String> roomInformation = room.get(roomId);
        roomInformation.replace((String)roomInformation.keySet().toArray()[0], nextTurn);
    }

    @Override
    public String getCurrentTurnByRoomId(int roomId) {
        Map<String, String> roomInformation = room.get(roomId);
        return (String) roomInformation.values().toArray()[0];
    }

    @Override
    public void removeTargetPiece(String target, int roomId) {
        piece.get(roomId).remove(target);
    }

    @Override
    public Map<Integer, String> getRoomNames() {
        Map<Integer, String> roomNames = new HashMap<>();
        for (Map.Entry<Integer, Map<String, String>> room : room.entrySet()) {
            roomNames.put(room.getKey(), (String) room.getValue().keySet().toArray()[0]);
        }
        return roomNames;
    }

    @Override
    public int getRoomId(String roomName) {
        Optional<Map.Entry<Integer, Map<String, String>>> first = room.entrySet().stream()
                .filter(entry -> entry.getValue().containsKey(roomName))
                .findFirst();
        return first.get().getKey();
    }
}
