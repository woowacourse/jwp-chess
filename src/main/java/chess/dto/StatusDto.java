package chess.dto;

import chess.domain.chessboard.position.Position;
import chess.domain.game.Player;
import chess.domain.piece.Piece;
import chess.domain.state.State;
import chess.domain.state.Status;
import chess.view.Square;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusDto {

    private final List<Square> squares;
    private final HashMap<Player, Double> results;

    private StatusDto(List<Square> squares, HashMap<Player, Double> results) {
        this.squares = squares;
        this.results = results;
    }

    public static StatusDto of(State state) {
        Status status = (Status) state;
        List<Square> squares = showChessBoard(state.getBoard());
        HashMap<Player, Double> results = status.calculateScore();
        return new StatusDto(squares, results);
    }

    private static List<Square> showChessBoard(final Map<Position, Piece> board) {
        final List<Square> squares = new ArrayList<>();
        for (final Position position : board.keySet()) {
            addPiece(position, board.get(position), squares);
        }
        return squares;
    }

    private static void addPiece(final Position position, final Piece piece, final List<Square> squares) {
        if (!piece.isBlank()) {
            squares.add(new Square(piece.getImageName(), position.getPosition()));
        }
    }

    public List<Square> getSquares() {
        return squares;
    }

    public Double getScore(Player player) {
        return results.get(player);
    }
}
