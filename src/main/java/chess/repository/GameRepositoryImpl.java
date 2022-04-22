package chess.repository;

import chess.dao.SquareDao;
import chess.dao.StateDao;
import chess.dto.MoveDto;
import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;
import chess.model.state.State;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private final SquareDao squareDao;
    private final StateDao stateDao;

    public GameRepositoryImpl(SquareDao squareDao, StateDao stateDao) {
        this.squareDao = squareDao;
        this.stateDao = stateDao;
    }

    public void initGameData(State state) {
        deleteGameData();
        stateDao.insert(state);
        insertBoard(state.getBoard());
    }

    public void saveGameData(State nextState, MoveDto moveDto) {
        Board board = squareDao.createBoard();
        State nowState = stateDao.find(board);
        stateDao.update(nowState, nextState);

        String source = moveDto.getSource();
        String target = moveDto.getTarget();
        Map<Position, Piece> squares = nextState.getBoard();

        squareDao.update(Position.from(source), squares.get(Position.from(source)));
        squareDao.update(Position.from(target), squares.get(Position.from(target)));
    }

    public void deleteGameData() {
        squareDao.delete();
        stateDao.delete();
    }

    public Board getBoard() {
        return squareDao.createBoard();
    }

    public State getState() {
        Board board = squareDao.createBoard();
        return stateDao.find(board);
    }

    private void insertBoard(Map<Position, Piece> board) {
        board.keySet()
                .forEach(position -> squareDao.insert(position, board.get(position)));
    }
}
