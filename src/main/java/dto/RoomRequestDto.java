package dto;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RoomRequestDto {
    private Long id;
    @Size(min = 2, max = 8)
    private String name;
    @Size(min = 4, max = 8)
    private String pw;
    private Long gameId;

    public RoomRequestDto(final Long id, @Min(value = 2) @NotNull final String name, @Min(value = 10) @NotNull final String pw, final Long gameId) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.gameId = gameId;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(final String pw) {
        this.pw = pw;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(final Long gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "RoomRequestDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pw='" + pw + '\'' +
                ", gameId=" + gameId +
                '}';
    }
}
