package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wooteco.chess.domain.Color;
import wooteco.chess.repository.entity.GameEntity;
import wooteco.chess.repository.entity.RoomEntity;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends CrudRepository<RoomEntity, Long> {
    @Override
    List<RoomEntity> findAll();
}
