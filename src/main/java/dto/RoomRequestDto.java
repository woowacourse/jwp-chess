package dto;
import javax.validation.constraints.Min;

public class RoomRequestDto {
    private Long id;
    @Min(value = 2)
    private String name;
    @Min(value = 4)
    private String pw;
    private Long gameId;

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
