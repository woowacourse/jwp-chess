package chess.dto.response;

import chess.dto.BoardsDto;
import chess.entity.RoomEntity;

public class EnterResponseDto {
    private String name;
    private String team;
    private BoardsDto board;


    private EnterResponseDto(final String name, final String team, final BoardsDto board) {
        this.name = name;
        this.team = team;
        this.board = board;
    }

    public static EnterResponseDto of(final RoomEntity room, final BoardsDto boards) {
        return new EnterResponseDto(room.getName(), room.getTeam(), boards);
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public BoardsDto getBoard() {
        return board;
    }
}
