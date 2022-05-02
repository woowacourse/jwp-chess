package chess.repository;

import static java.util.stream.Collectors.toMap;

import chess.model.board.Board;
import chess.model.board.Square;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import chess.repository.dao.PieceDao;
import chess.repository.dao.entity.PieceEntity;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class BoardRepository {
    private final PieceDao pieceDao;

    public BoardRepository(final PieceDao pieceDao) {
        this.pieceDao = pieceDao;
    }

    public Board findById(final Integer gameId) {
        Map<Square, Piece> piecesWithSquare = pieceDao.getBoardByGameId(gameId)
                .stream()
                .collect(toMap(entity -> Square.of(entity.getSquare()), PieceType::createPiece));
        return new Board(piecesWithSquare);
    }

    public void initBoard(final Integer gameId) {
        pieceDao.initBoard(gameId);
    }

    public int update(final Integer gameId, final Square square, Piece piece) {
        return pieceDao.update(new PieceEntity(gameId, square, piece));
    }

}
