package chess.service.dto;

public class ChessSaveRequestDto {

    private String name;

    public ChessSaveRequestDto(){}

    public ChessSaveRequestDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
