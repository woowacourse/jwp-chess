package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.factory.PieceInitializer;
import chess.domain.position.Position;
import chess.domain.position.Source;
import chess.domain.position.Target;
import chess.domain.state.State;
import chess.domain.state.StateFactory;

public class ChessGame {
    private final State whitePlayer;
    private final State blackPlayer;
    private final Board chessBoard;
    private boolean isGameOver = false;

    public ChessGame(final State whitePlayer, final State blackPlayer, final Board chessBoard) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.chessBoard = chessBoard;
    }

    public static ChessGame newGame() {
        return new ChessGame(StateFactory.initialization(PieceInitializer.whitePieces()),
                StateFactory.initialization(PieceInitializer.blackPieces()), Board.emptyBoard() );
    }

/*    public void moveByTurn(final Position sourcePosition, final Position targetPosition) {
        if (whitePlayer.isFinish()) {
            Source source = new Source(blackPlayer.findPiece(sourcePosition).orElseThrow(() -> new IllegalArgumentException("현재 위치에 기물이 없습니다.")));
            Target target = new Target(  );
            blackPlayer.move(source, target, whitePlayer.getState());
            whitePlayer.toRunningState(blackPlayer.getState());
            checkPieces(whitePlayer.getState(), target);
            if(isGameOver){
                winner = Color.BLACK;
            }
            return;
        }
        Source source = Source.valueOf(sourcePosition, whitePlayer.getState());
        Target target = Target.valueOf(source, targetPosition, whitePlayer.getState());
        whitePlayer.move(source, target, blackPlayer.getState());
        blackPlayer.toRunningState(whitePlayer.getState());
        checkPieces(blackPlayer.getState(), target);
        if(isGameOver){
            winner = Color.WHITE;
        }
    }*/
}
