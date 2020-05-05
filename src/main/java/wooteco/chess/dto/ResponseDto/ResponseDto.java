package wooteco.chess.dto.ResponseDto;

import wooteco.chess.support.ChessResponseCode;
import wooteco.chess.support.ResponseCode;

import java.util.Collections;

public class ResponseDto<T> {
    private Integer responseCode;
    private String message;
    private T responseData;

    private ResponseDto(ResponseCode responseCode, String message) {
        this.responseCode = responseCode.getCode();
        this.message = message;
        this.responseData = (T) Collections.EMPTY_MAP;
    }

    private ResponseDto(ResponseCode responseCode) {
        this.responseCode = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.responseData = (T) Collections.EMPTY_MAP;
    }

    private ResponseDto(ResponseCode responseCode, T responseData) {
        this.responseCode = responseCode.getCode();
        this.message = responseCode.getMessage();
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

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponseData() {
        return responseData;
    }

    public void setResponseData(T responseData) {
        this.responseData = responseData;
    }
}
