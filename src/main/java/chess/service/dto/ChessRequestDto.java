package chess.service.dto;

public class ChessRequestDto {

    private final String title;
    private final String password;
    private String turn;
    private String status;

    public ChessRequestDto(final String title, final String password) {
        this.title = title;
        this.password = password;
    }

    public ChessRequestDto(final String title, final String password, final String turn, final String status) {
        this.title = title;
        this.password = password;
        this.turn = turn;
        this.status = status;
    }

    public void setTurn(final String turn) {
        this.turn = turn;
    }

    public void setStatus(final String status) {
        this.status = status;
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
