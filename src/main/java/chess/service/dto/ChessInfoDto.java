package chess.service.dto;

public class ChessInfoDto {

    private String name;

    public ChessInfoDto() {
    }

    public ChessInfoDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
