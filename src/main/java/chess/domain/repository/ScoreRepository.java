package chess.domain.repository;

import chess.dao.ScoreDao;
import chess.dao.dto.score.ScoreDto;
import chess.domain.entity.Score;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ScoreRepository implements ChessRepository<Score, Long> {

    private final ScoreDao scoreDao;
    private final ModelMapper modelMapper;

    public ScoreRepository(ScoreDao scoreDao, ModelMapper modelMapper) {
        this.scoreDao = scoreDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public Long save(Score score) {
        ScoreDto scoreDto = modelMapper.map(score, ScoreDto.class);
        return scoreDao.save(scoreDto);
    }

    @Override
    public Long update(Score score) {
        ScoreDto scoreDto = modelMapper.map(score, ScoreDto.class);
        return scoreDao.update(scoreDto);
    }

    @Override
    public Score findById(Long id) {
        ScoreDto scoreDto = scoreDao.findById(id);
        return modelMapper.map(scoreDto, Score.class);
    }

    public Score findByGameId(Long gameId) {
        ScoreDto scoreDto = scoreDao.findByGameId(gameId);
        return modelMapper.map(scoreDto, Score.class);
    }
}
