package chess.web.dto;

import chess.domain.game.Side;

public class MoveCommandDto {

    private String source;
    private String target;
    private Side turn;

    public MoveCommandDto() {
    }

    public MoveCommandDto(String source, String target, String turn) {
        this.source = source;
        this.target = target;
        this.turn = Side.valueOf(turn);
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
        this.turn = Side.valueOf(turn);
    }
}
