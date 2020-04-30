package chess.dto.view;

public class MoveDto {

    private String source;
    private String target;

    protected MoveDto() {
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
