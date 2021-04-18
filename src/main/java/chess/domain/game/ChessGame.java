package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.domain.piece.factory.PieceInitializer;
import chess.domain.player.BlackPlayer;
import chess.domain.player.Player;
import chess.domain.player.WhitePlayer;
import chess.domain.position.Position;
import chess.domain.position.Source;
import chess.domain.position.Target;
import chess.domain.state.State;
import chess.domain.state.StateFactory;
import chess.service.dto.ScoreDto;
import java.util.ArrayList;
import java.util.List;

public class ChessGame {

    private final Player whitePlayer;
    private final Player blackPlayer;
    private final GameOver gameOver;
    private Board chessBoard;

    public ChessGame(final Player whitePlayer, final Player blackPlayer, final Board chessBoard) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.chessBoard = chessBoard;
        this.gameOver = new GameOver();
    }

    public static ChessGame newGame() {
        final Board board = Board.emptyBoard();
        final State whitePlayerState = StateFactory.initialization(PieceInitializer.whitePieces());
        final State blackPlayerState = StateFactory.initialization(PieceInitializer.blackPieces());
        return new ChessGame(new WhitePlayer(whitePlayerState), new BlackPlayer(blackPlayerState),
            board.put(whitePlayerState.pieces(), blackPlayerState.pieces()));
    }

    public void
    moveByTurn(final Position sourcePosition, final Position targetPosition) {
        if (whitePlayer.isFinish()) {
            move(sourcePosition, targetPosition, blackPlayer);
            chessBoard = chessBoard.put(whitePlayer.pieces(), blackPlayer.pieces());
            return;
        }
        move(sourcePosition, targetPosition, whitePlayer);
        chessBoard = chessBoard.put(whitePlayer.pieces(), blackPlayer.pieces());
    }

    public boolean isGameOver() {
        return gameOver.isGameOver();
    }

    public Pieces pieces() {
        List<Piece> pieces = new ArrayList<>();
        pieces.addAll(whitePlayer.pieces().pieces());
        pieces.addAll(blackPlayer.pieces().pieces());
        return new Pieces(pieces);
    }

    private void move(final Position sourcePosition, final Position targetPosition, Player player) {
        Source source = new Source(
            player.findPiece(sourcePosition).orElseThrow(() -> new IllegalArgumentException("본인 턴에 맞는 기물을 선택해 주세요.")));
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
            gameOver.changeGameOver();
        }
        if (state.findPiece(target.position()).isPresent()) {
            state.removePiece(target.position());
        }
    }

    public Board chessBoard() {
        return chessBoard;
    }

    public ScoreDto calculateScore() {
        return new ScoreDto(score(whitePlayer.pieces()), score(blackPlayer.pieces()));
    }

    public Color findWinner() {
        double whiteScore = score(whitePlayer.pieces());
        double blackScore = score(blackPlayer.pieces());

        if (whiteScore > blackScore) {
            return Color.WHITE;
        }

        if (whiteScore < blackScore) {
            return Color.BLACK;
        }
        return Color.NOTHING;
    }

    private double score(final Pieces pieces) {
        return pieces.calculateScore();
    }
}
