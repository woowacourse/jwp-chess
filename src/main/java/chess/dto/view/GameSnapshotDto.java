package chess.dto.view;

import chess.domain.board.Board;
import chess.domain.board.piece.Piece;
import chess.domain.board.position.Position;
import chess.domain.board.position.Rank;
import chess.domain.game.GameState;
import chess.dto.view.board.RowDto;
import chess.util.GameStateDisplayUtil;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class GameSnapshotDto {

    private final GameState state;
    private final List<RowDto> board;

    private GameSnapshotDto(GameState state, List<RowDto> board) {
        this.state = state;
        this.board = board;
    }

    public static GameSnapshotDto of(GameState state, Board board) {
        return new GameSnapshotDto(state, toBoardDisplay(board.toMap()));
    }

    private static List<RowDto> toBoardDisplay(Map<Position, Piece> board) {
        return Rank.allRanksDescending()
                .stream()
                .map(rank -> new RowDto(board, rank))
                .collect(Collectors.toList());
    }

    public GameState getState() {
        return state;
    }

    public String getStateValue() {
        return GameStateDisplayUtil.toDisplay(state);
    }

    public List<RowDto> getBoard() {
        return board;
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
        return "GameSnapshotDto{" + "state=" + state + ", board=" + board + '}';
    }
}
