package chess.domain;

import chess.domain.piece.Piece;
import chess.domain.team.BlackTeam;
import chess.domain.team.Team;
import chess.domain.team.WhiteTeam;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class ChessGame {
    private final BlackTeam blackTeam;
    private final WhiteTeam whiteTeam;
    private Team currentTurn;
    private boolean isEnd;

    public ChessGame(final WhiteTeam whiteTeam, final BlackTeam blackTeam) {
        this.whiteTeam = whiteTeam;
        this.blackTeam = blackTeam;

        whiteTeam.setEnemy(blackTeam);
        blackTeam.setEnemy(whiteTeam);

        this.currentTurn = this.whiteTeam;
        this.currentTurn.startTurn();
        this.isEnd = false;
    }

    public ChessGame(final WhiteTeam whiteTeam, final BlackTeam blackTeam, boolean isEnd) {
        this.whiteTeam = whiteTeam;
        this.blackTeam = blackTeam;
        this.isEnd = isEnd;
        setCurrentTurn();
    }

    public void move(final Position current, final Position destination) {
        final Piece chosenPiece = currentTurn.choosePiece(current);
        validateMovable(current, destination, chosenPiece);

        Team enemy = currentTurn.getEnemy();
        if (enemy.havePiece(destination)) {
            killEnemyPiece(destination, enemy);
        }

        currentTurn.move(current, destination);
        changeTurn();
    }

    private void validateMovable(final Position current, final Position destination, final Piece chosenPiece) {
        if (currentTurn.havePiece(destination) || !chosenPiece.isMovable(current, destination, generateChessBoard())) {
            throw new IllegalArgumentException("이동할 수 없습니다.");
        }
    }

    private void killEnemyPiece(Position destination, Team enemy) {
        Piece piece = enemy.killPiece(destination);
        checkMate(piece);
    }

    private void checkMate(Piece piece) {
        if (piece.isKing()) {
            finish();
        }
    }

    public void finish() {
        isEnd = true;
    }

    public boolean isEnd() {
        return isEnd;
    }

    private void changeTurn() {
        currentTurn.endTurn();
        currentTurn = currentTurn.getEnemy();
        currentTurn.startTurn();
    }

    public Map<Position, Piece> generateChessBoard() {
        final Map<Position, Piece> chessBoard = blackTeam.getPiecePosition();
        chessBoard.putAll(whiteTeam.getPiecePosition());
        return Collections.unmodifiableMap(chessBoard);
    }

    public BlackTeam getBlackTeam() {
        return blackTeam;
    }

    public WhiteTeam getWhiteTeam() {
        return whiteTeam;
    }

    private void setCurrentTurn() {
        if (blackTeam.isCurrentTurn()) {
            currentTurn = blackTeam;
            return;
        }

        currentTurn = whiteTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return isEnd == chessGame.isEnd && Objects.equals(blackTeam, chessGame.blackTeam)
            && Objects.equals(whiteTeam, chessGame.whiteTeam) && Objects
            .equals(currentTurn, chessGame.currentTurn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blackTeam, whiteTeam, currentTurn, isEnd);
    }
}
