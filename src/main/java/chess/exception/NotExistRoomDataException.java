package chess.exception;

public class NotExistRoomDataException extends NotExistDataException {

    private static final String MESSAGE = "[Error] DB에 방 목록 데이터가 없습니다.";

    public NotExistRoomDataException(){
        super(MESSAGE);
    }

}
