package wooteco.chess.dto;

//TODO : roomId 제거
public class MoveDto {
    private Long roomId;
    private String source;
    private String target;

    protected MoveDto() {
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
