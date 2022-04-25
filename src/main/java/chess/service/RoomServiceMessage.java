package chess.service;

public enum RoomServiceMessage {
    ROOM_CREATE_SUCCESS("새로운 체스방이 생성되었습니다."),
    ROOM_CREATE_FAIL_BY_DUPLICATED_NAME("이미 동일한 이름의 체스방이 존재합니다.")
    ;

    private final String alertMessage;

    RoomServiceMessage(final String alertMessage) {
        this.alertMessage = alertMessage;
    }
}
