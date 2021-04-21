package chess.domain.dto.response;

public class ApiResponseDto<T> {
    private ApiResponseCode code;
    private String message;
    private T data;

    private ApiResponseDto() {
    }

    private ApiResponseDto(ApiResponseCode status, T data) {
        this.bindStatus(status);
        this.data = data;
    }

    private ApiResponseDto(ApiResponseCode status, T data, String message) {
        this.code = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponseDto<T> createOK(T data) {
        return new ApiResponseDto<>(ApiResponseCode.OK, data);
    }

    public static <T> ApiResponseDto<T> BAD_REQUEST(T data, String message) {
        return new ApiResponseDto<>(ApiResponseCode.BAD_REQUEST, data, message);
    }

    private void bindStatus(ApiResponseCode status) {
        this.code = status;
        this.message = status.getMessage();
    }

    public ApiResponseCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}

