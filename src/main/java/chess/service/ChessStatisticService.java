package chess.service;

import chess.dto.StatusRecordDto;
import chess.entity.StatusRecordEntity;
import chess.repository.StatusRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChessStatisticService {
	private final StatusRecordRepository statusRecordRepository;

	public ChessStatisticService(final StatusRecordRepository statusRecordRepository) {
		this.statusRecordRepository = statusRecordRepository;
	}

	public List<StatusRecordDto> loadStatusRecordsWithRoomName() {
		List<StatusRecordEntity> statusRecordEntities = statusRecordRepository.findAll();
		return statusRecordEntities.stream()
				.map(entity -> new StatusRecordDto(entity.getRecord(),
						entity.getGameDate(), entity.getRoomName()))
				.collect(Collectors.toList());
	}
}
