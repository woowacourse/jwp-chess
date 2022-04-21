package chess.dto;

public class SuccessResponseDto {

    private final Object body;

    public SuccessResponseDto(Object body) {
        this.body = body;
    }

    public boolean getOk() {
        return true;
    }

    public String getError() {
        return null;
    }

    public Object getBody() {
        return body;
    }
}
