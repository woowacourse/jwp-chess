package chess.dto.response;

import chess.dto.BoardsDto;
import chess.entity.RoomEntity;

public class GameResponseDto {
    private Long id;
    private String name;
    private String team;
    private boolean gameOver;
    private BoardsDto board;


    private GameResponseDto(final Long id, final String name, final String team, final boolean gameOver, final BoardsDto board) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.gameOver = gameOver;
        this.board = board;
    }

    public static GameResponseDto of(final RoomEntity room, final BoardsDto boards) {
        return new GameResponseDto(room.getId(), room.getName(), room.getTeam(), room.isGameOver(), boards);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public BoardsDto getBoard() {
        return board;
    }
}
