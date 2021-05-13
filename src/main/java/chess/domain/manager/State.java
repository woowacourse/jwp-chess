package chess.domain.manager;

import chess.domain.board.piece.Owner;

public class State {

    private static final Owner FIRST_TURN_OWNER = Owner.WHITE;

    private final Owner turnOwner;
    private final TurnNumber turnNumber;
    private final boolean isPlaying;

    private State(final Owner turnOwner, final TurnNumber turnNumber, final boolean isPlaying) {
        this.turnOwner = turnOwner;
        this.turnNumber = turnNumber;
        this.isPlaying = isPlaying;
    }

    public static State of(final Owner turnOwner, final TurnNumber turnNumber, final boolean isPlaying) {
        return new State(turnOwner, turnNumber, isPlaying);
    }

    public static State newGameState() {
        return new State(FIRST_TURN_OWNER, TurnNumber.getFirstTurnNumber(), true);
    }

    public State changeTurnOwner() {
        if (this.turnOwner.isSame(Owner.BLACK)) {
            return new State(this.turnOwner.reverse(), this.turnNumber.increaseNumber(), this.isPlaying);
        }
        return new State(this.turnOwner.reverse(), this.turnNumber, this.isPlaying);
    }

    public String turnOwnerName() {
        return this.turnOwner.name();
    }

    public int turnNumberValue() {
        return turnNumber.toInt();
    }

    public State endGame() {
        return new State(this.turnOwner, this.turnNumber, false);
    }

    public Owner turnOwner() {
        return this.turnOwner;
    }

    public TurnNumber turnNumber() {
        return this.turnNumber;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
