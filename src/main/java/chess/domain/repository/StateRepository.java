package chess.domain.repository;

import chess.dao.StateDao;
import chess.dao.dto.state.StateDto;
import chess.domain.entity.State;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StateRepository implements ChessRepository<State, Long> {

    private final StateDao stateDao;
    private final ModelMapper modelMapper;

    public StateRepository(StateDao stateDao, ModelMapper modelMapper) {
        this.stateDao = stateDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public Long save(State state) {
        StateDto stateDto = modelMapper.map(state, StateDto.class);
        return stateDao.save(stateDto);
    }

    @Override
    public Long update(State state) {
        StateDto stateDto = modelMapper.map(state, StateDto.class);
        return stateDao.update(stateDto);
    }

    @Override
    public State findById(Long id) {
        StateDto stateDto = stateDao.findById(id);
        return modelMapper.map(stateDto, State.class);
    }

    public State findByGameId(Long gameId) {
        StateDto stateDto = stateDao.findByGameId(gameId);
        return modelMapper.map(stateDto, State.class);
    }
}
