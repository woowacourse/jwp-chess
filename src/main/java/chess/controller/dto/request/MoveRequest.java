package chess.controller.dto.request;

import javax.validation.constraints.NotNull;

public class MoveRequest {

    @NotNull(message = "이동할 말의 위치는 NULL 값일 수 없습니다.")
    private String start;
    @NotNull(message = "이동할 목표 위치는 NULL 값일 수 없습니다.")
    private String target;

    public MoveRequest() {
    }

    public MoveRequest(String start, String target) {
        this.start = start;
        this.target = target;
    }

    public String getStart() {
        return start;
    }

    public String getTarget() {
        return target;
    }
}
