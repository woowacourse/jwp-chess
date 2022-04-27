package chess.repository;

import chess.dto.MoveDto;
import chess.model.board.Board;
import chess.model.state.State;

public interface GameRepository {

    void initGameData(Long id, State state);

    void saveGameData(Long id, State nextState, MoveDto moveDto);

    void deleteGameData(Long id);

    Board getBoard(Long id);

    State getState(Long id);
}
