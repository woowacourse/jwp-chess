package chess.application.web.dto;

import chess.chessboard.position.Position;
import chess.game.Player;
import chess.piece.Piece;
import chess.state.State;
import chess.view.Square;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StateDto {

    private final List<Square> squares;
    private final Player player;

    private StateDto(List<Square> squares, Player player) {
        this.squares = squares;
        this.player = player;
    }

    public static StateDto of(State state) {
        List<Square> squares = showChessBoard(state.getBoard());
        Player player = state.getNextTurnPlayer();
        return new StateDto(squares, player);
    }

    public List<Square> getSquares() {
        return squares;
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

    public Player getPlayer() {
        return player;
    }
}
