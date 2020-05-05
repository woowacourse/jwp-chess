package wooteco.chess.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends CrudRepository<RoomEntity, Long> {

    @Override
    List<RoomEntity> findAll();

    @Override
    RoomEntity save(RoomEntity room);

    @Override
    Optional<RoomEntity> findById(Long id);

    @Override
    void deleteById(Long roomId);

}