package chess.exception;

public class WinnerIsNotExisting extends RuntimeException {

    public WinnerIsNotExisting() {
        super("아직 승자가 없습니다.");
    }
}
