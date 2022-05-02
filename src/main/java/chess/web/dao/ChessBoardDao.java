package chess.web.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import chess.web.dto.ChessCellDto;
import chess.web.dto.MoveDto;
import java.util.Map;

public interface ChessBoardDao {

    void save(Position position, Piece piece, int roomId);

    Map<Position, Piece> findAllPieces(int roomId);

    void movePiece(MoveDto moveDto, int roomId);

    ChessCellDto findByPosition(int roomId, String position);

    boolean boardExistInRoom(int roomId);
}
