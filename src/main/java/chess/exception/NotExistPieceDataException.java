package chess.exception;

public class NotExistPieceDataException extends NotExistDataException {

    private static final String MESSAGE = "[Error] DB에 체스 기물 데이터가 없습니다.";

    public NotExistPieceDataException(){
        super(MESSAGE);
    }

}
