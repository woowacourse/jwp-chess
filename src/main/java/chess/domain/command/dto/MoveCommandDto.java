package chess.domain.command.dto;

import chess.domain.game.Side;
import java.util.Locale;

public class MoveCommandDto {

    private String source;
    private String target;
    private Side turn;

    public MoveCommandDto() {
    }

    public MoveCommandDto(String source, String target, String turn) {
        this.source = source;
        this.target = target;
        this.turn = convertToSide(turn);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Side getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = convertToSide(turn);
    }

    private Side convertToSide(String side) {
        return Side.valueOf(side.toUpperCase(Locale.ROOT));
    }
}
