package chess.repository;

import chess.dto.MoveDto;
import chess.model.board.Board;
import chess.model.state.State;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

    private final SquareRepository squareRepository;
    private final StateRepository stateRepository;

    public GameRepository(SquareRepository squareRepository, StateRepository stateRepository) {
        this.squareRepository = squareRepository;
        this.stateRepository = stateRepository;
    }

    public void initGameData(final String id, final State state) {
        squareRepository.initSquareData(id, state.getBoard());
        stateRepository.initStateData(id, state);
    }

    public void deleteGameDataFrom(final String id) {
        squareRepository.deleteSquareDataFrom(id);
        stateRepository.deleteStateDataFrom(id);
    }

    public void saveGameData(final String id, final State nextState, final MoveDto moveDto) {
        Board board = squareRepository.getBoardFrom(id);
        State nowState = stateRepository.getStateFrom(id, board);
        squareRepository.updateSquareData(id, nextState.getBoard(), moveDto);
        stateRepository.updateStateData(id, nowState, nextState);
    }

    public Board getBoardFrom(final String id) {
        return squareRepository.getBoardFrom(id);
    }

    public State getStateFrom(final String id) {
        Board board = squareRepository.getBoardFrom(id);
        return stateRepository.getStateFrom(id, board);
    }
}
