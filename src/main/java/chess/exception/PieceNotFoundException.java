package chess.exception;

public class PieceNotFoundException extends RuntimeException {

    private static final String PIECE_NOT_FOUND_MESSAGE = "해당 위치에 말이 존재하지 않습니다.";

    public PieceNotFoundException() {
        super(PIECE_NOT_FOUND_MESSAGE);
    }
}
