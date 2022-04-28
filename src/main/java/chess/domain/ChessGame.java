package chess.domain;

import chess.domain.chessboard.ChessBoard;
import chess.domain.chessboard.ChessBoardFactory;
import chess.domain.position.Position;
import chess.exception.IllegalCommandException;
import chess.result.EndResult;
import chess.result.MoveResult;
import chess.result.StartResult;

public class ChessGame {

    private ChessBoard chessBoard;
    private GameStatus gameStatus;

    public ChessGame(final ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
        this.gameStatus = GameStatus.READY;
    }

    public ChessGame(final ChessBoard chessBoard, final GameStatus gameStatus) {
        this.chessBoard = chessBoard;
        this.gameStatus = gameStatus;
    }

    public MoveResult move(final Position from, final Position to) {
        checkMove();

        final MoveResult result = chessBoard.move(from, to);
        if (chessBoard.isKingDie()) {
            gameStatus = GameStatus.KING_DIE;
            result.changeStatusToKingDie();
        }
        return result;
    }

    private void checkMove() {
        if (!gameStatus.isPlaying()) {
            throw new IllegalCommandException("게임이 진행중일 때만 기물을 움직일 수 있습니다.");
        }
    }

    public Score calculateScore() {
        gameStatus.checkPlaying();
        return chessBoard.calculateScore();
    }

    public StartResult start() {
        gameStatus.checkReady();
        if (gameStatus.isEnd()) {
            chessBoard = ChessBoardFactory.createChessBoard();
        }
        gameStatus = GameStatus.PLAYING;
        return new StartResult(chessBoard.findAllPiece());
    }

    public void end() {
        checkEnd();
        gameStatus = GameStatus.END;
        final Score score = new Score(chessBoard.findAllPiece());
        new EndResult(score);
    }

    private void checkEnd() {
        if (gameStatus.isEnd()) {
            throw new IllegalCommandException("이미 게임이 종료되었습니다.");
        }
    }
}
