package chess.repository;

import chess.entity.StatusRecordEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatusRecordRepository extends CrudRepository<StatusRecordEntity, Integer> {

	@Override
	@Query("SELECT * FROM status_record ORDER BY game_date DESC")
	List<StatusRecordEntity> findAll();

	@Modifying
	@Query("INSERT INTO status_record(record, room_name) " +
			"VALUES(:record, (SELECT room_name FROM room WHERE room.id = :room_id))")
	void save(@Param("record") final String record, @Param("room_id") final int roomId);
}
