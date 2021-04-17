package chess.domain.response;

import java.util.Collection;
import java.util.List;

public class Response {
    private StatusEnum status;
    private Object data;

    public Response() {}

    public Response(final StatusEnum status, final Object data) {
        this.status = status;
        this.data = data;
    }

    public Response(Object data) {
        this.status = StatusEnum.OK;
        this.data = data;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }
}
