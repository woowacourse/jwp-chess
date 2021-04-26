package dto;

public class MoveDto {
    String from;
    String to;

    public MoveDto() {}

    public MoveDto(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
