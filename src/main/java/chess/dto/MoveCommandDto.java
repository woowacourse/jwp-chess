package chess.dto;

public class MoveCommandDto {

    private String rawFrom;
    private String rawTo;

    public MoveCommandDto() {
    }

    public MoveCommandDto(final String rawFrom, final String rawTo) {
        this.rawFrom = rawFrom;
        this.rawTo = rawTo;
    }

    public String getRawFrom() {
        return rawFrom;
    }

    public String getRawTo() {
        return rawTo;
    }
}
