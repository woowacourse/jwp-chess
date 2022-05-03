package chess.dto;

public class DeleteDto {

    private int id;
    private String pw;

    public DeleteDto() {
    }

    public DeleteDto(int id, String pw) {
        this.pw = pw;
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public int getId() {
        return id;
    }
}
