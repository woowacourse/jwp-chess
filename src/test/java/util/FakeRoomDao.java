package util;

import chess.dao.RoomDao;
import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.response.CurrentTurnDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomStatusDto;
import java.util.ArrayList;
import java.util.List;

public class FakeRoomDao implements RoomDao {

    @Override
    public int save(final String roomName, final GameStatus gameStatus, final Color currentTurn,
                    final String password) {
        return 1;
    }

    @Override
    public boolean isExistName(final String roomName) {
        return false;
    }

    @Override
    public boolean isExistId(final int roomId) {
        return false;
    }

    @Override
    public List<RoomResponseDto> findAll() {
        final List<RoomResponseDto> dtos = new ArrayList<>();

        dtos.add(RoomResponseDto.of(1, "test1", GameStatus.READY.getValue()));
        dtos.add(RoomResponseDto.of(2, "test2", GameStatus.PLAYING.getValue()));
        dtos.add(RoomResponseDto.of(3, "test3", GameStatus.END.getValue()));
        dtos.add(RoomResponseDto.of(4, "test4", GameStatus.KING_DIE.getValue()));

        return dtos;
    }

    @Override
    public String findPasswordById(final int roomId) {
        return null;
    }

    @Override
    public CurrentTurnDto findCurrentTurnById(final int roomId) {
        return null;
    }

    @Override
    public RoomStatusDto findStatusById(final int roomId) {
        return null;
    }

    @Override
    public int deleteById(final int roomId) {
        return 0;
    }

    @Override
    public int updateById(final int roomId, final GameStatus gameStatus, final Color currentTurn) {
        return 0;
    }

    @Override
    public int updateStatusById(final int roomId, final GameStatus gameStatus) {
        return 0;
    }
}
