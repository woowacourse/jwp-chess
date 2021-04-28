package chess.repository;

import chess.domain.chessgame.Room;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository {

    void insertRoom(Room room);

    void insertPieces(Room room);

    List<Integer> findAllPlayingRoomId();

    Room findRoomByRoomId(int roomId);

    Boolean findPlayingFlagByRoomId(int roomId);

    Map<Position, Piece> findPiecesByRoomId(int roomId);

    Color findTurnByRoomId(int roomId);

    void updateChessGameByRoom(Room room);

    void updatePiecesByRoom(Room room);

    void deleteAllPiecesByRoom(Room room);

}
