package chess.dto;

import chess.entity.BoardEntity;
import java.util.List;
import java.util.stream.Collectors;

public class BoardsDto {

    private List<BoardDto> boards;

    private BoardsDto(final List<BoardDto> boards) {
        this.boards = boards;
    }

    public static BoardsDto of(final List<BoardEntity> boards) {
        final List<BoardDto> boardDtos = boards.stream()
            .map(BoardDto::of)
            .collect(Collectors.toList());
        return new BoardsDto(boardDtos);
    }

    public List<BoardDto> getBoards() {
        return boards;
    }
}
