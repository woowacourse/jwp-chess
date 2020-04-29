package wooteco.chess.domain.room;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {

    @Override
    List<Room> findAll();

    @Override
    Room save(Room room);

    @Override
    Optional<Room> findById(Long id);

    @Override
    void deleteById(Long roomId);

}
