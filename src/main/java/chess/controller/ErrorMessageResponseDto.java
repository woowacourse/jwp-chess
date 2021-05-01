package chess.controller;

class ErrorMessageResponseDto {
    private final String errorMsg;

    public ErrorMessageResponseDto(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
