package chess.dto;

import chess.domain.room.Room;

public class RoomDto {
    private Long id;
    private RoomInfoDto roomInfo;
    private PlayersDto players;
    private boolean enterable;

    public RoomDto(Room room) {
        this.id = room.getId();
        this.roomInfo = new RoomInfoDto(room.getName(), room.getPw(), room.getGameId());
        this.players = new PlayersDto(room.getWhitePlayer(), room.getBlackPlayer());
        this.enterable = room.enterable();
    }

    public Long getId() {
        return id;
    }

    public RoomInfoDto getRoomInfo() {
        return roomInfo;
    }

    public PlayersDto getPlayers() {
        return players;
    }

    public boolean isEnterable() {
        return enterable;
    }
}
