package chess.service;

import chess.dao.StatusRecordDao;
import chess.dto.StatusRecordDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ChessStatisticService {
	private final StatusRecordDao statusRecordDao;

	public ChessStatisticService(final StatusRecordDao statusRecordDao) {
		this.statusRecordDao = statusRecordDao;
	}

	public List<StatusRecordDto> loadStatusRecordsWithRoomName() throws SQLException {
		return statusRecordDao.findStatusRecords();
	}

}
