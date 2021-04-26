package chess.advice;

public enum ExceptionState {
    ILLEGAL_ARGUMENT(400, "잘못된 요청입니다."),
    NULL_POINTER(400, "잘못된 참조입니다."),
    EMPTY_RESULT_DATA_ACCESS(400, "없는 데이터에 엑세스 했습니다."),
    EXCEPTION(400, "기타 에러입니다.");

    private final int status;
    private final String errorMessage;

    ExceptionState(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
