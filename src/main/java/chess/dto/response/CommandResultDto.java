package chess.dto.response;

public class CommandResultDto {
    private final boolean isSuccess;
    private final String message;

    private CommandResultDto(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public static CommandResultDto of(boolean isSuccess, String message) {
        return new CommandResultDto(isSuccess, message);
    }

    public boolean isSuccess() {
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
