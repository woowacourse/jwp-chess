package chess.domain.repository;

import chess.dao.GameDao;
import chess.dao.dto.game.GameDto;
import chess.domain.entity.Game;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GameRepository implements ChessRepository<Game, Long> {

    private final GameDao gameDao;
    private final ModelMapper modelMapper;

    public GameRepository(GameDao gameDao, ModelMapper modelMapper) {
        this.gameDao = gameDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public Long save(Game game) {
        GameDto gameDto = modelMapper.map(game, GameDto.class);
        return gameDao.save(gameDto);
    }

    @Override
    public Long update(Game game) {
        GameDto gameDto = modelMapper.map(game,GameDto.class);
        return gameDao.update(gameDto);
    }

    @Override
    public Game findById(Long id) {
        GameDto gameDto = gameDao.findById(id);
        return modelMapper.map(gameDto, Game.class);
    }

    public List<Game> findByPlayingIsTrue() {
        List<GameDto> gameDtos = gameDao.findByPlayingIsTrue();
        return gameDtos.stream()
                .map(gameDto -> modelMapper.map(gameDto, Game.class))
                .collect(Collectors.toList());
    }
}
