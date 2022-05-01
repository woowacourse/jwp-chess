package chess.dao;

import chess.domain.Team;
import chess.dto.request.GameIdRequest;
import chess.dto.request.MakeRoomRequest;
import chess.dto.response.RoomResponse;
import chess.dto.response.RoomStatusResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FakeRoomDao implements RoomDao {

    private final Map<Long, RoomResponse> games = new HashMap<>();

    private long id = 0L;

    @Override
    public void makeGame(Team team, MakeRoomRequest makeRoomRequest) {
        id++;
        games.put(id, new RoomResponse(id, team, makeRoomRequest.getName(), makeRoomRequest.getPassword()));
    }

    @Override
    public List<RoomResponse> getGames() {
        return games.keySet()
                .stream()
                .map(games::get)
                .collect(Collectors.toList());
    }

    @Override
    public RoomStatusResponse findById(MakeRoomRequest makeRoomRequest) {
        RoomResponse room = games
                .keySet()
                .stream()
                .filter(key -> games.get(key).getName()
                        .equals(makeRoomRequest.getName()))
                .map(games::get)
                .findAny()
                .orElse(null);
        if (room != null) {
            return new RoomStatusResponse(room.getId(), room.status());
        }
        return null;
    }

    @Override
    public RoomResponse findById(GameIdRequest gameIdRequest) {
        return games.keySet()
                .stream()
                .filter(key -> Objects.equals(games.get(key).getId(), gameIdRequest.getId()))
                .map(games::get)
                .findAny()
                .orElse(null);
    }

    @Override
    public boolean isExistId(GameIdRequest gameIdRequest) {
        return games.containsKey(gameIdRequest.getId());
    }

    @Override
    public void updateStatus(Team team, long roomId) {
        for (Long idx : games.keySet()) {
            updateIfAvailable(team, roomId, idx);
        }
    }

    private void updateIfAvailable(Team team, long roomId, Long idx) {
        if (games.get(idx).getId() == roomId) {
            games.put(idx, new RoomResponse(games.get(idx).getId(),
                    team, games.get(idx).getName(), games.get(idx).getPassword()));
        }
    }

    @Override
    public void deleteGame(long roomId) {
        games.remove(roomId);
    }
}
