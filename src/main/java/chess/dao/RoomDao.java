package chess.dao;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.response.CurrentTurnDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomStatusDto;
import java.util.List;

public interface RoomDao {

    int save(String roomName, GameStatus gameStatus, Color currentTurn, String password);

    boolean isExistName(String roomName);

    boolean isExistId(int roomId);

    List<RoomResponseDto> findAll();

    String findPasswordById(int roomId);

    CurrentTurnDto findCurrentTurnById(int roomId);

    RoomStatusDto findStatusById(int roomId);

    int deleteById(int roomId);

    int updateById(int roomId, GameStatus gameStatus, Color currentTurn);

    int updateStatusById(int roomId, GameStatus gameStatus);
}
