package chess.dto;

public class ResponseDto {

    private final int statusCode;
    private final String errorMessage;

    public ResponseDto(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String toJson() {
        return "{" + "\"statusCode\" : " + statusCode
                + ", \"errorMessage\" : \"" + errorMessage + "\"}";
    }
}
