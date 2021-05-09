package chess.service;

import chess.domain.history.History;
import chess.domain.repository.history.HistoryRepository;
import chess.service.dto.history.HistoryDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<HistoryDto> findHistoriesByGameId(final Long gameId) {
        List<History> histories = historyRepository.findByGameId(gameId);
        return histories.stream()
                .map(HistoryDto::from)
                .collect(Collectors.toList());
    }
}
