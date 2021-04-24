package chess.repository;

import java.util.List;

import chess.domain.board.BoardDto;
import chess.domain.room.Room;

public interface RoomRepository {
    List<Room> findAll();

    long findChessIdById(long roomId);

    long insert(Room room, BoardDto boardDto);
}
