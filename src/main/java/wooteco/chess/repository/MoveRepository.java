package wooteco.chess.repository;

import wooteco.chess.dto.MoveDto;
import wooteco.chess.result.Result;

import java.sql.SQLException;

public interface MoveRepository {
    wooteco.chess.result.Result add(MoveDto moveDto) throws SQLException;

    wooteco.chess.result.Result findById(int moveId) throws SQLException;

    wooteco.chess.result.Result findByRoomId(int roomId) throws SQLException;

    wooteco.chess.result.Result deleteById(int moveId) throws SQLException;

    wooteco.chess.result.Result deleteByRoomId(int roomId) throws SQLException;

    wooteco.chess.result.Result deleteAll() throws SQLException;
}
