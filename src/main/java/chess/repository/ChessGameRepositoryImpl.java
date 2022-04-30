package chess.repository;

import chess.dao.ChessPieceDao;
import chess.dao.RoomDao;
import chess.domain.ChessGame;
import chess.domain.chessboard.ChessBoard;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.position.Position;
import chess.entity.ChessPieceEntity;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameRepositoryImpl implements ChessGameRepository {

    private final ChessPieceDao chessPieceDao;
    private final RoomDao roomDao;

    public ChessGameRepositoryImpl(final ChessPieceDao chessPieceDao, final RoomDao roomDao) {
        this.chessPieceDao = chessPieceDao;
        this.roomDao = roomDao;
    }

    @Override
    public ChessGame get(final int roomId) {
        final Map<Position, ChessPiece> pieceByPosition = chessPieceDao.findAllEntityByRoomId(roomId)
                .stream()
                .collect(Collectors.toMap(
                        ChessPieceEntity::toPosition,
                        ChessPieceEntity::toChessPiece
                ));
        final ChessBoard chessBoard = new ChessBoard(pieceByPosition);
        return new ChessGame(chessBoard, roomDao.findStatus(roomId));
    }

    @Override
    public void update(final int roomId, final Position from, final Position to) {
        chessPieceDao.deleteByRoomIdAndPosition(roomId, to);
        chessPieceDao.updateByRoomIdAndPosition(roomId, from, to);
    }

    @Override
    public void add(final int roomId, final ChessGame chessGame) {
        final ChessBoard chessBoard = chessGame.getChessBoard();
        chessPieceDao.saveAll(roomId, chessBoard.findAllPiece());
    }
}