package chess.repository;

import chess.dto.StatusRecordDto;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatusRecordRepository extends CrudRepository<StatusRecordDto, Integer> {

	@Override
	@Query("SELECT * FROM status_record ORDER BY game_date DESC")
	List<StatusRecordDto> findAll();
}
