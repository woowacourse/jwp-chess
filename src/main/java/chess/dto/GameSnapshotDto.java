package chess.dto;

import chess.dto.board.RowDto;
import chess.dto.board.WebBoardViewDto;
import java.util.List;
import java.util.Objects;

public class GameSnapshotDto {

    private final GameStateDto game;
    private final WebBoardViewDto board;

    public GameSnapshotDto(GameStateDto game, WebBoardViewDto board) {
        this.game = game;
        this.board = board;
    }

    public GameStateDto getGame() {
        return game;
    }

    public List<RowDto> getBoard() {
        return board.toDisplay();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameSnapshotDto gameDto = (GameSnapshotDto) o;
        return Objects.equals(game, gameDto.game)
                && Objects.equals(board, gameDto.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, board);
    }

    @Override
    public String toString() {
        return "GameSnapshotDto{" + "game=" + game + ", board=" + board + '}';
    }
}
