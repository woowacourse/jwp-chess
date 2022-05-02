package chess.web.dto;

public class MoveDto {
    private int id;
    private String source;
    private String target;

    public MoveDto() {
    }

    public MoveDto(int id, String source, String target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public int getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
