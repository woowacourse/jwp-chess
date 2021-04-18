package chess.domain.piece;

import chess.domain.board.ChessBoard;
import chess.domain.board.Direction;
import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.feature.Type;

import java.util.List;

public class Blank extends Piece {

    public Blank(Color color, Position position) {
        super(Color.NO_COLOR, position);
        if (!color.equals(Color.NO_COLOR)) {
            throw new BlankColorException();
        }
        this.type = Type.BLANK;
    }

    public Blank(Position position) {
        super(position);
        this.type = Type.BLANK;
    }

    @Override
    public void move(ChessBoard chessBoard, Direction direction, Position targetPosition) {
        throw new BlankMoveException();
    }

    @Override
    public boolean isMovable(final ChessBoard chessBoard, final Direction direction, final Position targetPosition) {
        throw new BlankMoveException();
    }

    @Override
    public List<Direction> directions() {
        throw new BlankMoveException();
    }
}
