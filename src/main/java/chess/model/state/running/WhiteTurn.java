package chess.model.state.running;

import static chess.model.Team.WHITE;

import chess.dto.MoveDto;
import chess.model.board.Board;
import chess.model.position.Position;
import chess.model.state.State;
import chess.model.state.finished.End;

public final class WhiteTurn extends Running {

    public WhiteTurn(Board board) {
        super(board);
    }

    @Override
    public State proceed(final MoveDto moveDto) {
        final String source = moveDto.getSource();
        final String target = moveDto.getTarget();
        movePiece(source, target);
        return createStateByBoard();
    }

    private void movePiece(final String source, final String target) {
        board.checkSameTeam(WHITE, Position.from(source));
        board.move(Position.from(source), Position.from(target));
    }

    private State createStateByBoard() {
        if (board.isKingDead()) {
            return new End(board);
        }
        return new BlackTurn(board);
    }

    public String getSymbol() {
        return "WHITE_TURN";
    }
}
