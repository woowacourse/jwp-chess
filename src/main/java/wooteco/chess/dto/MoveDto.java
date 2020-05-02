package wooteco.chess.dto;

public class MoveDto {
    private Long roomNumber;
    private String source;
    private String target;

    protected MoveDto() {
    }

    public MoveDto(Long roomNumber, String source, String target) {
        this.roomNumber = roomNumber;
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public Long getRoomNumber() {
        return roomNumber;
    }
}
