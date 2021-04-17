package chess.console.controller.dto.request;

public class CommandRequestDto {
    private final String commandInput;
    private final String startPositionInput;
    private final String destinationInput;

    public CommandRequestDto(String commandInput, String startPositionInput,
        String destinationInput) {

        this.commandInput = commandInput;
        this.startPositionInput = startPositionInput;
        this.destinationInput = destinationInput;
    }

    public CommandRequestDto(String commandInput) {
        this(commandInput, null, null);
    }

    public String getCommandInput() {
        return commandInput;
    }

    public String getStartPositionInput() {
        return startPositionInput;
    }

    public String getDestinationInput() {
        return destinationInput;
    }
}
