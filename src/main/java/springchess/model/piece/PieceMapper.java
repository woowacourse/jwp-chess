package springchess.model.piece;

@FunctionalInterface
public interface PieceMapper {

    Piece mapToPiece(Integer id, Team team, Integer squareId);
}
