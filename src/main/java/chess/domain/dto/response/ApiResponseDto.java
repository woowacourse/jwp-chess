package chess.domain.dto.response;

public class ApiResponseDto<T> {
    private ApiResponseCode code;
    private String message;
    private T data;

    private ApiResponseDto() {}

    private ApiResponseDto(ApiResponseCode status) {
        this.bindStatus(status);
    }

    private ApiResponseDto(ApiResponseCode status, T data) {
        this.bindStatus(status);
        this.data = data;
    }

    private void bindStatus(ApiResponseCode status) {
        this.code = status;
        this.message = status.getMessage();
    }

    public static <T> ApiResponseDto<T> createOK(T data) {
        return new ApiResponseDto<>(ApiResponseCode.OK, data);
    }

    public static <T> ApiResponseDto<T> NOT_FOUND(T data) {
        return new ApiResponseDto<>(ApiResponseCode.NOT_FOUND, data);
    }

    public static <T> ApiResponseDto<T> BAD_REQUEST(T data){
        return new ApiResponseDto<>(ApiResponseCode.BAD_REQUEST, data);
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

