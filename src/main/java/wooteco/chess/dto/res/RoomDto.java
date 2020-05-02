package wooteco.chess.dto.res;

import wooteco.chess.db.entity.RoomEntity;

public class RoomDto {
    private Long id;
    private String name;
    private String player1Name;
    private String player2Name;

    public RoomDto(Long id, String player1Name, String player2Name) {
        this.id = id;
        this.name = "체스 " + id + "번방";
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    public RoomDto(RoomEntity roomEntity) {
        this.id = roomEntity.getId();
        this.name = "체스 " + id + "번방";
    }

    public RoomDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }
}
