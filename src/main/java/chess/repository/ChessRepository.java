package chess.repository;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.Map;

public interface ChessRepository {
    int makeRoom(Map<String, String> board, String roomName);

    void initializePieceStatus(int roomId, final Map<String, String> board);

    Map<Position, Piece> getBoardByRoomId(int roomId);

    void moveSourcePieceToTargetPoint(String source, String target, int roomId);

    void changeTurn(String nextTurn, String currentTurn, int roomId);

    String getCurrentTurnByRoomId(int roomId);

    void removeTargetPiece(String target, int roomId);

    Map<Integer, String> getRoomNames();

    int getRoomId(String roomName);

}