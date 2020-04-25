package wooteco.chess.repository;

import wooteco.chess.dto.RoomDto;
import wooteco.chess.result.Result;

import java.sql.SQLException;

public interface RoomRepository {
    wooteco.chess.result.Result create(RoomDto roomDto) throws SQLException;

    wooteco.chess.result.Result findById(int roomId) throws SQLException;

    wooteco.chess.result.Result findByName(String roomName) throws SQLException;

    wooteco.chess.result.Result update(RoomDto roomDto) throws SQLException;

    wooteco.chess.result.Result delete(int roomId) throws SQLException;

    wooteco.chess.result.Result deleteAll() throws SQLException;
}
