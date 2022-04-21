package chess.dto;

public class SuccessResponseDto2 {

    private final Object body;

    public SuccessResponseDto2(Object body) {
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
