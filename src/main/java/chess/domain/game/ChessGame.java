package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.piece.factory.PieceInitializer;
import chess.domain.position.Position;
import chess.domain.position.Source;
import chess.domain.position.Target;
import chess.domain.state.State;
import chess.domain.state.StateFactory;

public class ChessGame {
    private final Board chessBoard;
    private State whitePlayer;
    private State blackPlayer;
    private boolean isGameOver = false;

    public ChessGame(final State whitePlayer, final State blackPlayer, final Board chessBoard) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.chessBoard = chessBoard;
    }

    public static ChessGame newGame() {
        final Board board = Board.emptyBoard();
        final State whitePlayer = StateFactory.initialization(PieceInitializer.whitePieces());
        final State blackPlayer = StateFactory.initialization(PieceInitializer.blackPieces());
        return new ChessGame(whitePlayer, blackPlayer, board.put(whitePlayer.pieces(), blackPlayer.pieces()));
    }

    public void moveByTurn(final Position sourcePosition, final Position targetPosition) {
        if (whitePlayer.isFinish()) {
            Source source = new Source(blackPlayer.findPiece(sourcePosition).orElseThrow(() -> new IllegalArgumentException("현재 위치에 기물이 없습니다.")));
            Target target = new Target(chessBoard.findPiece(targetPosition));
            blackPlayer = blackPlayer.move(source, target);
            whitePlayer = whitePlayer.toRunningState(blackPlayer);
            checkPieces(whitePlayer, target);
            if (isGameOver) {
            }
            return;
        }
        Source source = new Source(whitePlayer.findPiece(sourcePosition).orElseThrow(() -> new IllegalArgumentException("현재 위치에 기물이 없습니다.")));
        Target target = new Target(chessBoard.findPiece(targetPosition));
        whitePlayer = whitePlayer.move(source, target);
        blackPlayer = blackPlayer.toRunningState(whitePlayer);
        checkPieces(blackPlayer, target);
        if (isGameOver) {
        }
    }

    public void move() {

    }

    private void checkPieces(final State state, final Target target) {
        if (state.isKingPosition(target.getPiece().position())) {
            this.isGameOver = true;
        }
        if (state.findPiece(target.getPiece().position()).isPresent()) {
            state.removePiece(target.getPiece().position());
        }
    }
}
