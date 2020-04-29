package wooteco.chess.dto;

import wooteco.chess.support.ChessResponseCode;
import wooteco.chess.support.ResponseCode;

import java.util.Collections;

public class ResponseDto<T> {
    private String responseCode;
    private String errorMessage;
    private T responseData;

    public ResponseDto(ResponseCode responseCode, String errorMessage) {
        this.responseCode = responseCode.getCode();
        this.errorMessage = errorMessage;
        this.responseData = (T) Collections.EMPTY_MAP;
    }

    public ResponseDto(ResponseCode responseCode) {
        this.responseCode = responseCode.getCode();
        this.errorMessage = responseCode.getMessage();
        this.responseData = (T) Collections.EMPTY_MAP;
    }

    public ResponseDto(ResponseCode responseCode, T responseData) {
        this.responseCode = responseCode.getCode();
        this.responseData = responseData;
    }

    public static ResponseDto success() {
        return new ResponseDto(ChessResponseCode.SUCCESS);
    }

    public static <T> ResponseDto<T> success(T responseData) {
        return new ResponseDto<>(ChessResponseCode.SUCCESS, responseData);
    }

    public static <T> ResponseDto<T> success(ResponseCode responseCode) {
        return new ResponseDto<>(responseCode, responseCode.getMessage());
    }

    public static ResponseDto fail() {
        return new ResponseDto(ChessResponseCode.BAD_REQUEST);
    }

    public static <T> ResponseDto<T> fail(ResponseCode responseCode) {
        return new ResponseDto<>(responseCode, responseCode.getMessage());
    }

}
