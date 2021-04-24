package chess.exception.domain;

import chess.exception.BusinessException;
import chess.exception.ErrorCode;

public class InvalidRoomCommandException extends BusinessException {

    public InvalidRoomCommandException(String message) {
        super(message, ErrorCode.INVALID_ROOM_COMMAND);
    }
}
