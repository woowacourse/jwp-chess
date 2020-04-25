package wooteco.chess.repository;

import wooteco.chess.dto.RoomDto;
import wooteco.chess.result.Result;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CachedRoomRepository implements RoomRepository {
    private static RoomRepository roomRepository = new RoomRepositoryImpl();
    private static Map<Integer, RoomDto> cachedRoom = new HashMap<>();

    @Override
    public wooteco.chess.result.Result create(RoomDto roomDto) throws SQLException {
        return roomRepository.create(roomDto);
    }

    @Override
    public wooteco.chess.result.Result findById(int roomId) throws SQLException {
        if (cachedRoom.containsKey(roomId)) {
            return new wooteco.chess.result.Result(true, cachedRoom.get(roomId));
        }
        wooteco.chess.result.Result result = roomRepository.findById(roomId);
        if (result.isSuccess()) {
            RoomDto roomDto = (RoomDto) result.getObject();
            cachedRoom.put(roomId, roomDto);
            return new wooteco.chess.result.Result(true, roomDto);
        }
        return new wooteco.chess.result.Result(false, null);
    }

    @Override
    public wooteco.chess.result.Result findByName(final String roomName) throws SQLException {
        wooteco.chess.result.Result result = roomRepository.findByName(roomName);
        if (result.isSuccess()) {
            RoomDto roomDto = (RoomDto) result.getObject();
            cachedRoom.put(roomDto.getRoomId(), roomDto);
            return new wooteco.chess.result.Result(true, roomDto);
        }
        return new wooteco.chess.result.Result(false, null);
    }


    @Override
    public wooteco.chess.result.Result update(final RoomDto roomDto) throws SQLException {
        wooteco.chess.result.Result result = roomRepository.update(roomDto);
        if (result.isSuccess()) {
            cachedRoom.put(roomDto.getRoomId(), roomDto);
        }
        return result;
    }

    @Override
    public wooteco.chess.result.Result delete(final int roomId) throws SQLException {
        wooteco.chess.result.Result result = roomRepository.delete(roomId);
        if (result.isSuccess()) {
            cachedRoom.remove(roomId);
        }
        return result;
    }

    @Override
    public wooteco.chess.result.Result deleteAll() throws SQLException {
        return roomRepository.deleteAll();
    }
}
