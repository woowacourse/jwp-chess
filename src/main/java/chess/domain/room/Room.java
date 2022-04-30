package chess.domain.room;

import chess.domain.piece.Color;

public class Room {

    private static final String INVALID_ROOM_PASSWORD_EXCEPTION_MESSAGE = "잘못된 방 비밀번호 입니다. 다시 입력해주세요.";
    private static final String NOT_FINISHED_GAME_STATUS_EXCEPTION_MESSAGE = "아직 게임이 끝나지 않아 삭제할 수 없습니다.";

    private final long id;
    private final boolean endFlag;
    private final Color turn;
    private final String title;
    private final String password;

    public Room(long id, boolean endFlag, Color turn, String title, String password) {
        this.id = id;
        this.endFlag = endFlag;
        this.turn = turn;
        this.title = title;
        this.password = password;
    }

    public void validateDeletable(String password) {
        validatePassword(password);
        validateEnd();
    }

    private void validatePassword(String password) {
        if (!this.password.equals(password)) {
            throw new IllegalArgumentException(INVALID_ROOM_PASSWORD_EXCEPTION_MESSAGE);
        }
    }

    private void validateEnd() {
        if (!endFlag) {
            throw new IllegalArgumentException(NOT_FINISHED_GAME_STATUS_EXCEPTION_MESSAGE);
        }
    }

    public long getId() {
        return id;
    }

    public Color getTurn() {
        return turn;
    }

    public boolean getEndFlag() {
        return endFlag;
    }

    public String getTitle() {
        return title;
    }

}
