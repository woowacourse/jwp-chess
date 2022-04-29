package chess.dao.dto;

public class GameDto {

    private Long id;
    private String title;
    private String password;
    private String turn;
    private String status;

    public GameDto() {
    }

    public GameDto(String title, String password, String turn, String status) {
        this.title = title;
        this.password = password;
        this.turn = turn;
        this.status = status;
    }

    public GameDto(Long id, String title, String password, String turn, String status) {
        this.id = id;
        this.title = title;
        this.password = password;
        this.turn = turn;
        this.status = status;
    }

    public GameDto(Long id, String title, String turn, String status) {
        this.id = id;
        this.title = title;
        this.password = "";
        this.turn = turn;
        this.status = status;
    }

    public GameDto(Long id, String password) {
        this.id = id;
        this.title = "";
        this.password = password;
        this.turn = "";
        this.status = "";
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public String getTurn() {
        return turn;
    }

    public String getStatus() {
        return status;
    }
}
