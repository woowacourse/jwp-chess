package chess.web.dao;

import chess.domain.state.StateType;
import chess.web.dto.GameResponseDto;
import chess.web.dto.CreateGameRequestDto;
import java.util.List;

public interface GameDao {

    int save(CreateGameRequestDto createGameRequestDto, StateType stateType);

    void updateStateById(int id, StateType stateType);

    StateType findStateById(int id);

    String findPasswordById(int id);

    List<GameResponseDto> findAll();

    void deleteGameById(int id);
}