package dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RoomRequestDto {
    final private Long id;
    @Size(min = 2, max = 8)
    final private String name;
    @Size(min = 4, max = 8)
    final private String pw;
    final private String user;
    final private Long gameId;

    public RoomRequestDto(final Long id, @Min(value = 2) @NotNull final String name, @Min(value = 10) @NotNull final String pw, final String user, final Long gameId) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.user = user;
        this.gameId = gameId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPw() {
        return pw;
    }

    public String getUser() { return user; }

    public Long getGameId() {
        return gameId;
    }

    @Override
    public String toString() {
        return "RoomRequestDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pw='" + pw + '\'' +
                ", user='" + user + '\'' +
                ", gameId=" + gameId +
                '}';
    }
}
