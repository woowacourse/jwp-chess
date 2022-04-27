package chess.repository;

import java.util.Map;

import org.springframework.stereotype.Repository;

import chess.dao.SquareDao;
import chess.dao.StateDao;
import chess.dto.MoveDto;
import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;
import chess.model.state.State;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private final SquareDao squareDao;
    private final StateDao stateDao;

    public GameRepositoryImpl(SquareDao squareDao, StateDao stateDao) {
        this.squareDao = squareDao;
        this.stateDao = stateDao;
    }

    public void initGameData(Long id, State state) {
        deleteGameData(id);
        stateDao.insert(id, state);
        insertBoard(id, state.getBoard());
    }

    public void saveGameData(Long id, State nextState, MoveDto moveDto) {
        Board board = squareDao.createBoard(id);
        State nowState = stateDao.find(id, board);
        stateDao.update(id, nowState, nextState);

        String source = moveDto.getSource();
        String target = moveDto.getTarget();
        Map<Position, Piece> squares = nextState.getBoard();

        squareDao.update(id, Position.from(source), squares.get(Position.from(source)));
        squareDao.update(id, Position.from(target), squares.get(Position.from(target)));
    }

    public void deleteGameData(Long id) {
        squareDao.delete(id);
        stateDao.delete(id);
    }

    public Board getBoard(Long id) {
        return squareDao.createBoard(id);
    }

    public State getState(Long id) {
        Board board = squareDao.createBoard(id);
        return stateDao.find(id, board);
    }

    private void insertBoard(Long id, Map<Position, Piece> board) {
        board.keySet()
            .forEach(position -> squareDao.insert(id, position, board.get(position)));
    }
}
