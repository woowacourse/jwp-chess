package chess.repository;

import chess.dao.SquareDao;
import chess.dto.MoveDto;
import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;
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
        Map<Position, Piece> squares = squareDao.findSquaresFrom(id);
        return Board.from(squares);
    }

    public void deleteSquareDataFrom(final String id) {
        squareDao.deleteFrom(id);
    }
}
