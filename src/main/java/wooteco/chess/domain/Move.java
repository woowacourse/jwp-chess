package wooteco.chess.domain;

import org.springframework.data.annotation.Id;

public class Move {
    @Id private int id;
    private int game;
    private String startPosition;
    private String endPosition;

    public Move(final int game, final String startPosition, final String endPosition) {
        this.game = game;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public int getId() {
        return id;
    }

    public int getGame() {
        return game;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public String getEndPosition() {
        return endPosition;
    }
}
