package chess.dto;

public class MoveForm {

    private final String source;
    private final String target;

    public MoveForm() {
        this(null, null);
    }

    public MoveForm(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
