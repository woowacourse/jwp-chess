package chess.dto.response;

import chess.dto.BoardsDto;
import chess.entity.RoomEntity;

public class GameResponseDto {
    private String name;
    private String team;
    private boolean gameOver;
    private BoardsDto board;


    private GameResponseDto(final String name, final String team, final boolean gameOver, final BoardsDto board) {
        this.name = name;
        this.team = team;
        this.gameOver = gameOver;
        this.board = board;
    }

    public static GameResponseDto of(final RoomEntity room, final BoardsDto boards) {
        return new GameResponseDto(room.getName(), room.getTeam(), room.isGameOver(), boards);
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
