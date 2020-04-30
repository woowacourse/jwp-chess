package chess.dto;

public class MoveDto {

    private String source;
    private String target;

    public MoveDto() {
    }

    public MoveDto(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }


    @Override
    public String toString() {
        return "MoveDto{" +
            "source='" + source + '\'' +
            ", target='" + target + '\'' +
            '}';
    }
}
