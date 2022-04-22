package chess.repository;

import java.util.List;

public interface RoomRepository<T> {

    List<T> findAllWithRunning();

    int deleteAll();

    T save(T room);

    T getById(int roomId);
}
