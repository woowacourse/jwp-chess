package chess.service.dto;

public enum GameStatusDto {

    READY("ready"),
    PLAYING("playing"),
    FINISHED("finished");

    private final String name;

    GameStatusDto(final String name) {
        this.name = name;
    }

    public static boolean isFinished(final String status) {
        return FINISHED.name.equals(status);
    }

    public String getName() {
        return name;
    }
}
