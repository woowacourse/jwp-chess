package chess.dto;

public class MoveResponseDto {

    String source;
    String target;
    String color;

    public MoveResponseDto(String source, String target, String color) {
        this.source = source;
        this.target = target;
        this.color = color;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
