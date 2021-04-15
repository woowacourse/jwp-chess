package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.piece.factory.PieceInitializer;
import chess.domain.player.BlackPlayer;
import chess.domain.player.Player;
import chess.domain.player.WhitePlayer;
import chess.domain.position.Position;
import chess.domain.position.Source;
import chess.domain.position.Target;
import chess.domain.state.State;
import chess.domain.state.StateFactory;

public class ChessGame {

    private Board chessBoard;
    private Player whitePlayer;
    private Player blackPlayer;
    private boolean isGameOver = false;

    public ChessGame(final Player whitePlayer, final Player blackPlayer, final Board chessBoard) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.chessBoard = chessBoard;
    }

    public static ChessGame newGame() {
        final Board board = Board.emptyBoard();
        final State whitePlayerState = StateFactory.initialization(PieceInitializer.whitePieces());
        final State blackPlayerState = StateFactory.initialization(PieceInitializer.blackPieces());
        return new ChessGame(new WhitePlayer(whitePlayerState), new BlackPlayer(blackPlayerState),
            board.put(whitePlayerState.pieces(), blackPlayerState.pieces()));
    }

    public void moveByTurn(final Position sourcePosition, final Position targetPosition) {
        if (whitePlayer.isFinish()) {
            move(sourcePosition, targetPosition, blackPlayer);
            chessBoard = chessBoard.put(whitePlayer.pieces(), blackPlayer().pieces());
            if (isGameOver) {
            }
            return;
        }
        move(sourcePosition, targetPosition, whitePlayer);
        chessBoard = chessBoard.put(whitePlayer.pieces(), blackPlayer.pieces());
        if (isGameOver) {
        }
    }

    private void move(final Position sourcePosition, final Position targetPosition, Player player) {
        Source source = new Source(
            player.findPiece(sourcePosition).orElseThrow(() -> new IllegalArgumentException("현재 위치에 기물이 없습니다.")));
        Target target = new Target(chessBoard.findPiece(targetPosition));

        if (chessBoard.checkPath(source, target)) {
            player.move(source, target);
            Player anotherPlayer = anotherPlayer(player);
            anotherPlayer.toRunningState(player.state());
            checkPieces(anotherPlayer.state(), target);
            return;
        }
        throw new IllegalArgumentException("해당 위치로 이동할 수 없습니다.");
    }

    private Player anotherPlayer(final Player player) {
        if (player.isBlack()) {
            return whitePlayer;
        }
        return blackPlayer;
    }

    private void checkPieces(final State state, final Target target) {
        if (state.isKingPosition(target.position())) {
            this.isGameOver = true;
        }
        if (state.findPiece(target.position()).isPresent()) {
            state.removePiece(target.position());
        }
    }

    public Player whitePlayer() {
        return whitePlayer;
    }

    public Player blackPlayer() {
        return blackPlayer;
    }

    public Board chessBoard() {
        return chessBoard;
    }
}
