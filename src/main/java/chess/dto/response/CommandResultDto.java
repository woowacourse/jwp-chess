package chess.dto.response;

public class CommandResultDto {
    private final String message;

    public CommandResultDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CommandResultDto{" +
            " message='" + message + '\'' +
            '}';
    }
}
