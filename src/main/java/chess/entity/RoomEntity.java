package chess.entity;

import chess.domain.GameStatus;

public class RoomEntity {

    private final Long id;
    private final String name;
    private final String password;
    private final String status;
    private final String turn;

    public RoomEntity(final String name, final String password, final String status, final String turn) {
        this(null, name, password, status, turn);
    }

    public RoomEntity(final Long id, final String name, final String password, final String status, final String turn) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.status = status;
        this.turn = turn;
    }

    public boolean isEnd() {
        return GameStatus.from(this.status) == GameStatus.END || GameStatus.from(this.status) == GameStatus.KING_DIE;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public String getTurn() {
        return turn;
    }
}
