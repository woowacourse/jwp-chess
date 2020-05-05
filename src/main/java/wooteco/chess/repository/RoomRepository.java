package wooteco.chess.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends CrudRepository<RoomEntity, UUID> {
    @Query("SELECT id FROM room WHERE name = :name")
    Optional<UUID> findIdByName(@Param("name") String name);

    @Override
    Optional<RoomEntity> findById(UUID id);

    @Query("SELECT name FROM room")
    List<String> findAllRoomNames();

    @Override
    List<RoomEntity> findAll();

    @Override
    <S extends RoomEntity> S save(S entity);
}
