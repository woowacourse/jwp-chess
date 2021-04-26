package chess.domain.repository;

import chess.dao.HistoryDao;
import chess.dao.dto.history.HistoryDto;
import chess.domain.entity.History;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class HistoryRepository implements ChessRepository<History, Long> {

    private final HistoryDao historyDao;
    private final ModelMapper modelMapper;

    public HistoryRepository(HistoryDao historyDao, ModelMapper modelMapper) {
        this.historyDao = historyDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public Long save(History history) {
        HistoryDto historyDto = modelMapper.map(history, HistoryDto.class);
        return historyDao.save(historyDto);
    }

    @Override
    public Long update(History history) {
        return null;
    }

    @Override
    public History findById(Long id) {
        return null;
    }

    public List<History> findByGameId(Long gameId) {
        List<HistoryDto> historyDtos = historyDao.findByGameId(gameId);
        return historyDtos.stream()
                .map(historyDto -> modelMapper.map(historyDto, History.class))
                .collect(Collectors.toList());
    }
}
