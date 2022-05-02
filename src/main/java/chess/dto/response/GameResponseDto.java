package chess.dto.response;

import chess.dto.BoardDto;
import chess.dto.BoardsDto;
import chess.entity.BoardEntity;
import chess.entity.RoomEntity;

import java.util.List;
import java.util.stream.Collectors;

public class GameResponseDto {
    private Long id;
    private String name;
    private String team;
    private boolean gameOver;
    private List<BoardDto> boards;


    private GameResponseDto(final Long id, final String name, final String team, final boolean gameOver, final List<BoardDto> boards) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.gameOver = gameOver;
        this.boards = boards;
    }

    public static GameResponseDto of(final RoomEntity room, final List<BoardEntity> board) {
        final List<BoardDto> boardDtos = board.stream()
                .map(BoardDto::of)
                .collect(Collectors.toList());
        return new GameResponseDto(room.getId(), room.getName(), room.getTeam(), room.isGameOver(), boardDtos);
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

    public List<BoardDto> getBoards() {
        return boards;
    }
}
