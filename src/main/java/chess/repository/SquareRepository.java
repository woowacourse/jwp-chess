package chess.repository;

import chess.dao.SquareDao;
import chess.dto.MoveDto;
import chess.entity.SquareEntity;
import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class SquareRepository {

    private final SquareDao squareDao;

    public SquareRepository(final SquareDao squareDao) {
        this.squareDao = squareDao;
    }

    public void initSquareData(final String id, final Map<Position, Piece> board) {
        squareDao.deleteFrom(id);
        board.keySet()
                .forEach(position -> squareDao.insert(id, position, board.get(position)));
    }

    public void updateSquareData(final String id, final Map<Position, Piece> board, final MoveDto moveDto) {
        String source = moveDto.getSource();
        String target = moveDto.getTarget();
        squareDao.update(id, Position.from(source), board.get(Position.from(source)));
        squareDao.update(id, Position.from(target), board.get(Position.from(target)));
    }

    public Board getBoardFrom(final String id) {
        List<SquareEntity> squareEntities = squareDao.findSquaresFrom(id);
        return createBoard(squareEntities);
    }

    private Board createBoard(final List<SquareEntity> squareEntities) {
        final Map<Position, Piece> squares = new HashMap<>();
        for (SquareEntity squareEntity : squareEntities) {
            Position position = Position.from(squareEntity.getPosition());
            Piece piece = Piece.getPiece(squareEntity.getTeam(), squareEntity.getSymbol());
            squares.put(position, piece);
        }
        return Board.from(squares);
    }

    public void deleteSquareDataFrom(final String id) {
        squareDao.deleteFrom(id);
    }
}
