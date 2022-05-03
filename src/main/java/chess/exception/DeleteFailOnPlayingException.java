package chess.exception;

public class DeleteFailOnPlayingException extends RuntimeException {

    private static final String CAN_NOT_DELETE_PLAYING_GAME = "[ERROR] 진행중인 게임은 삭제할 수 없습니다.";

    public DeleteFailOnPlayingException() {
        super(CAN_NOT_DELETE_PLAYING_GAME);
    }
}
