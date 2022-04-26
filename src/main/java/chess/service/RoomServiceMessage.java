package chess.service;

public enum RoomServiceMessage {
    ROOM_CREATE_SUCCESS("새로운 체스방이 생성되었습니다."),
    ROOM_CREATE_FAIL_BY_DUPLICATED_NAME("이미 동일한 이름의 체스방이 존재합니다."),

    ROOM_DELETE_SUCCESS("체스방이 삭제되었습니다."),
    ROOM_DELETE_FAIL_BY_WRONG_PASSWORD("비밀번호가 일치하지 않습니다."),
    ROOM_DELETE_FAIL_BY_NOT_END_GAME("게임이 진행중인 체스방은 삭제할 수 없습니다."),
    ;

    private final String alertMessage;

    RoomServiceMessage(final String alertMessage) {
        this.alertMessage = alertMessage;
    }
}
