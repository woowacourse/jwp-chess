package chess.dto.response;

public class CommandResultDto {
    private static final String SUCCESS_MESSAGE = "성공하였습니다.";

    private final boolean isSuccess;
    private final String message;

    private CommandResultDto(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public static CommandResultDto createSuccess() {
        return new CommandResultDto(true, SUCCESS_MESSAGE);
    }

    public static CommandResultDto createFail(String message) {
        return new CommandResultDto(false, message);
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CommandResultDto{" +
            "isSuccess=" + isSuccess +
            ", message='" + message + '\'' +
            '}';
    }
}
