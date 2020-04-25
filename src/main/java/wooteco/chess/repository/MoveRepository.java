package wooteco.chess.repository;

import wooteco.chess.dto.MoveDto;
import wooteco.chess.result.Result;

import java.sql.SQLException;

public interface MoveRepository {
    Result add(MoveDto moveDto) throws SQLException;

    Result findById(int moveId) throws SQLException;

    Result findByRoomId(int roomId) throws SQLException;

    Result deleteById(int moveId) throws SQLException;

    Result deleteByRoomId(int roomId) throws SQLException;

    Result deleteAll() throws SQLException;
}
