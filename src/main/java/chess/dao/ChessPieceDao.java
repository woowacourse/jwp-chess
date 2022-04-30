package chess.dao;

import chess.domain.chesspiece.ChessPiece;
import chess.domain.position.Position;
import chess.dto.response.ChessPieceDto;
import chess.entity.ChessPieceEntity;
import java.util.List;
import java.util.Map;

public interface ChessPieceDao {

    List<ChessPieceEntity> findAllEntityByRoomId(int roomId);

    List<ChessPieceDto> findAllByRoomId(int roomId);

    int deleteByRoomIdAndPosition(int roomId, Position position);

    int deleteAllByRoomId(int roomId);

    int saveAll(int roomId, Map<Position, ChessPiece> pieceByPosition);

    int updateByRoomIdAndPosition(int roomId, Position from, Position to);
}
