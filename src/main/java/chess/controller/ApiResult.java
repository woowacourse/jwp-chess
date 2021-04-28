package chess.controller;

import chess.domain.dto.ResponseStatus;

public class ApiResult<T> {

    private final ResponseStatus status;
    private final T payload;

    public ApiResult(ResponseStatus status, T payload) {
        this.status = status;
        this.payload = payload;
    }

    public static <T> ApiResult of(T payload) {
        return new ApiResult(ResponseStatus.OK, payload);
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public T getPayload() {
        return payload;
    }
}
