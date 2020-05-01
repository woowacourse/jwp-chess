package chess.service;

import chess.dto.StatusRecordDto;
import chess.repository.StatusRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChessStatisticService {
	private final StatusRecordRepository statusRecordRepository;

	public ChessStatisticService(final StatusRecordRepository statusRecordRepository) {
		this.statusRecordRepository = statusRecordRepository;
	}

	public List<StatusRecordDto> loadStatusRecordsWithRoomName() {
		return statusRecordRepository.findAll();
	}
}
