package chess.repository;

import chess.dto.MoveDto;
import chess.model.board.Board;
import chess.model.state.State;

public interface GameRepository {

    void initGameData(String id, State state);

    void saveGameData(String id, State nextState, MoveDto moveDto);

    void deleteGameDataFrom(String id);

    Board getBoardFrom(String id);

    State getStateFrom(String id);
}
