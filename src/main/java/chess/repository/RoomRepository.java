package chess.repository;

import chess.model.room.Room;
import chess.model.status.Status;

import java.util.List;

public interface RoomRepository<T> {

    List<Room> findAllByBoardStatus(Status status);

    T save(T room);

    T getById(int roomId);
}
