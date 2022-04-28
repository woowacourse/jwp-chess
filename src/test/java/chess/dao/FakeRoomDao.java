package chess.dao;

import chess.domain.Team;
import chess.dto.GameIdDto;
import chess.dto.MakeRoomDto;
import chess.dto.RoomDto;
import chess.dto.RoomStatusDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FakeRoomDao implements RoomDao {

    private final Map<Long, RoomDto> games = new HashMap<>();

    private long id = 0L;

    @Override
    public void makeGame(Team team, MakeRoomDto makeRoomDto) {
        id++;
        games.put(id, new RoomDto(id, team, makeRoomDto.getName(), makeRoomDto.getPassword()));
    }

    @Override
    public List<RoomDto> getGames() {
        return games.keySet()
                .stream()
                .map(games::get)
                .collect(Collectors.toList());
    }

    @Override
    public RoomStatusDto findById(MakeRoomDto makeRoomDto) {
        RoomDto room = games
                .keySet()
                .stream()
                .filter(key -> games.get(key).getName()
                        .equals(makeRoomDto.getName()))
                .map(games::get)
                .findAny()
                .orElse(null);
        if (room != null) {
            return new RoomStatusDto(room.getId(), room.getStatus());
        }
        return null;
    }

    @Override
    public RoomDto findById(GameIdDto gameIdDto) {
        return games
                .keySet()
                .stream()
                .filter(key -> Objects.equals(games.get(key).getId(), gameIdDto.getId()))
                .map(games::get)
                .findAny()
                .orElse(null);
    }

    @Override
    public void updateStatus(Team team, long roomId) {
        for (Long idx : games.keySet()) {
            if (games.get(idx).getId() == roomId) {
                games.put(idx, new RoomDto(games.get(idx).getId(),
                        team, games.get(idx).getName(), games.get(idx).getPassword()));
            }
        }
    }

    @Override
    public void deleteGame(long roomId) {
        games.remove(roomId);
    }
}
