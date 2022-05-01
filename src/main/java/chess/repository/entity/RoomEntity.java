package chess.repository.entity;

import chess.domain.piece.Color;
import chess.dto.GameCreateRequest;

public class RoomEntity {

    private long id;
    private String name;
    private String password;
    private String white;
    private String black;
    private String winner;
    private String looser;
    private Color turn;
    private boolean finished;
    private boolean deleted;

    public RoomEntity() {
    }

    public RoomEntity(long id, String name, String password, String white, String black, String winner, String looser,
                      Color turn, boolean finished, boolean deleted) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.white = white;
        this.black = black;
        this.winner = winner;
        this.looser = looser;
        this.turn = turn;
        this.finished = finished;
        this.deleted = deleted;
    }

    public RoomEntity(String name, String password, String white, String black) {
        this.name = name;
        this.password = password;
        this.white = white;
        this.black = black;
    }

    public RoomEntity(String name, String password, String white, String black, boolean finished) {
        this.name = name;
        this.password = password;
        this.white = white;
        this.black = black;
        this.finished = finished;
    }

    public static RoomEntity from(GameCreateRequest gameCreateRequest) {
        return new RoomEntity(gameCreateRequest.getRoomName(), gameCreateRequest.getRoomPassword(),
                gameCreateRequest.getWhiteName(), gameCreateRequest.getBlackName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getWhite() {
        return white;
    }

    public String getBlack() {
        return black;
    }

    public String getWinner() {
        return winner;
    }

    public String getLooser() {
        return looser;
    }

    public Color getTurn() {
        return turn;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isDeleted() {
        return deleted;
    }
}

