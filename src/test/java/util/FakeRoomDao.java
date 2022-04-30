package util;

import chess.dao.RoomDao;
import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.response.CurrentTurnDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomStatusDto;
import chess.entity.RoomEntity;
import chess.exception.NotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeRoomDao implements RoomDao {

    private final Map<Integer, Room> storage = new HashMap<>();
    private int series = 1;

    @Override
    public RoomEntity findById(final int roomId) {
        final Room room = storage.get(roomId);
        return toEntity(room);
    }

    private RoomEntity toEntity(final Room room) {
        return new RoomEntity(
                room.id,
                room.name,
                room.gameStatus.getValue(),
                room.currentTurn.getValue(),
                room.password,
                false
        );
    }

    @Override
    public List<RoomEntity> findAllEntity() {
        return storage.values()
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public int save(final String roomName, final GameStatus gameStatus, final Color currentTurn,
                    final String password) {
        final Room room = new Room(series, roomName, gameStatus, currentTurn, password);
        storage.put(series, room);
        return series++;
    }

    @Override
    public boolean isExistName(final String roomName) {
        return storage.values()
                .stream()
                .anyMatch(room -> room.name.equals(roomName));
    }

    @Override
    public boolean isExistId(final int roomId) {
        return storage.values()
                .stream()
                .anyMatch(room -> room.id == roomId);
    }

    @Override
    public List<RoomResponseDto> findAll() {
        return storage.values()
                .stream()
                .map(room -> RoomResponseDto.of(room.id, room.name, room.gameStatus.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public String findPasswordById(final int roomId) {
        return storage.values()
                .stream()
                .filter(room -> room.id == roomId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("방 아이디에 해당하는 비밀번호가 존재하지 않습니다."))
                .password;
    }

    @Override
    public CurrentTurnDto findCurrentTurnById(final int roomId) {
        return storage.values()
                .stream()
                .filter(room -> room.id == roomId)
                .findFirst()
                .map(room -> CurrentTurnDto.of(room.name, room.currentTurn))
                .orElseThrow(() -> new NotFoundException("방 아이디에 해당하는 턴 정보가 존재하지 않습니다."));
    }

    @Override
    public RoomStatusDto findStatusById(final int roomId) {
        return storage.values()
                .stream()
                .filter(room -> room.id == roomId)
                .findFirst()
                .map(room -> RoomStatusDto.of(room.name, room.gameStatus.getValue()))
                .orElseThrow(() -> new NotFoundException("방 아이디에 해당하는 게임 상태가 존재하지 않습니다."));
    }

    @Override
    public int deleteById(final int roomId) {
        if (!storage.containsKey(roomId)) {
            return 0;
        }
        storage.remove(roomId);
        return 1;
    }

    @Override
    public int updateById(final int roomId, final GameStatus gameStatus, final Color currentTurn) {
        if (!storage.containsKey(roomId)) {
            return 0;
        }

        final Room room = storage.get(roomId);
        room.gameStatus = gameStatus;
        room.currentTurn = currentTurn;
        storage.put(roomId, room);
        return 1;
    }

    @Override
    public int updateStatusById(final int roomId, final GameStatus gameStatus) {
        if (!storage.containsKey(roomId)) {
            return 0;
        }
        final Room room = storage.get(roomId);
        room.gameStatus = gameStatus;
        storage.put(roomId, room);
        return 1;
    }

    public void deleteAll() {
        storage.clear();
        series = 1;
    }

    private class Room {

        private final int id;
        private final String name;
        private final String password;
        private GameStatus gameStatus;
        private Color currentTurn;

        public Room(final int id, final String name, final GameStatus gameStatus, final Color currentTurn,
                    final String password) {
            this.id = id;
            this.name = name;
            this.gameStatus = gameStatus;
            this.currentTurn = currentTurn;
            this.password = password;
        }
    }
}
