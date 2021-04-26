package chess.domain.entity;

import chess.domain.manager.ChessManager;
import chess.domain.movecommand.MoveCommand;

import java.util.Objects;

public class History implements Entity<Long> {

    private Long id;
    private Long gameId;
    private String moveCommand;
    private String turnOwner;
    private int turnNumber;
    private boolean isPlaying;

    public History() {
    }

    public History(Long gameId, String moveCommand, String turnOwner, int turnNumber, boolean isPlaying) {
        this.gameId = gameId;
        this.moveCommand = moveCommand;
        this.turnOwner = turnOwner;
        this.turnNumber = turnNumber;
        this.isPlaying = isPlaying;
        validateHistory();
    }

    public static History of(Long gameId, MoveCommand moveCommand, ChessManager chessManager) {
        return new History(
                gameId,
                moveCommand.moveCommand(),
                chessManager.turnOwner().name(),
                chessManager.turnNumber(),
                chessManager.isPlaying());
    }

    private void validateHistory() {
        validateNull();
        validateEmpty();
    }

    private void validateNull() {
        Objects.requireNonNull(this.gameId, "gameId는 null일 수 없습니다.");
        Objects.requireNonNull(this.moveCommand, "moveCommand 는 null일 수 없습니다.");
        Objects.requireNonNull(this.turnOwner, "turnOwner 는 null일 수 없습니다.");
    }

    private void validateEmpty() {
        if (this.moveCommand.isEmpty()) {
            throw new IllegalArgumentException("moveCommand 는 빈값일 수 없습니다.");
        }
        if (this.turnOwner.isEmpty()) {
            throw new IllegalArgumentException("turnOwner 는 빈값일 수 없습니다.");
        }
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getMoveCommand() {
        return moveCommand;
    }

    public void setMoveCommand(String moveCommand) {
        this.moveCommand = moveCommand;
    }

    public String getTurnOwner() {
        return turnOwner;
    }

    public void setTurnOwner(String turnOwner) {
        this.turnOwner = turnOwner;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
