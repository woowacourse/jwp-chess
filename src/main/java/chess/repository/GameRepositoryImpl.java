package chess.repository;

import chess.dao.SquareDao;
import chess.dao.StateDao;
import chess.dto.MoveDto;
import chess.entity.SquareEntity;
import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;
import chess.model.state.State;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private final SquareDao squareDao;
    private final StateDao stateDao;

    public GameRepositoryImpl(final SquareDao squareDao, final StateDao stateDao) {
        this.squareDao = squareDao;
        this.stateDao = stateDao;
    }

    public void initGameData(final String id, final State state) {
        deleteGameDataFrom(id);
        stateDao.insert(id, state);
        insertBoard(id, state.getBoard());
    }

    private void insertBoard(final String id, final Map<Position, Piece> board) {
        board.keySet()
                .forEach(position -> squareDao.insert(id, position, board.get(position)));
    }

    public void saveGameData(final String id, final State nextState, final MoveDto moveDto) {
        List<SquareEntity> squareEntities = squareDao.findSquaresFrom(id);
        Board board = createBoard(squareEntities);
        State nowState = stateDao.find(id, board);
        stateDao.update(id, nowState, nextState);

        String source = moveDto.getSource();
        String target = moveDto.getTarget();
        Map<Position, Piece> squares = nextState.getBoard();

        squareDao.update(id, Position.from(source), squares.get(Position.from(source)));
        squareDao.update(id, Position.from(target), squares.get(Position.from(target)));
    }

    public void deleteGameDataFrom(final String id) {
        squareDao.deleteFrom(id);
        stateDao.deleteFrom(id);
    }

    public Board getBoardFrom(final String id) {
        List<SquareEntity> squareEntities = squareDao.findSquaresFrom(id);
        return createBoard(squareEntities);
    }

    public State getStateFrom(final String id) {
        List<SquareEntity> squareEntities = squareDao.findSquaresFrom(id);
        Board board = createBoard(squareEntities);
        return stateDao.find(id, board);
    }

    private Board createBoard(final List<SquareEntity> squareEntities) {
        final Map<Position, Piece> squares = new HashMap<>();
        for (SquareEntity squareEntity : squareEntities) {
            Position position = Position.from(squareEntity.getPosition());
            Piece piece = Piece.getPiece(squareEntity.getTeam() + "_" + squareEntity.getSymbol());
            squares.put(position, piece);
        }
        return Board.from(squares);
    }
}
