package chess.dto.view;

import chess.domain.game.statistics.GameState;
import chess.dto.view.board.BoardViewDto;
import chess.dto.view.board.RowDto;
import java.util.List;
import java.util.Objects;

public class GameSnapshotDto {

    private final GameState state;
    private final BoardViewDto board;

    public GameSnapshotDto(GameState state, BoardViewDto board) {
        this.state = state;
        this.board = board;
    }

    public GameState getState() {
        return state;
    }

    public String getStateValue() {
        return state.toDisplay();
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
        GameSnapshotDto that = (GameSnapshotDto) o;
        return state == that.state
                && Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, board);
    }

    @Override
    public String toString() {
        return "GameSnapshotDto{" +
                "state=" + state +
                ", board=" + board +
                '}';
    }
}
