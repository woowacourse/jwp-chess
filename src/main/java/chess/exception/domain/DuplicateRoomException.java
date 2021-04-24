package chess.exception.domain;

import chess.exception.BusinessException;
import chess.exception.ErrorCode;

public class DuplicateRoomException extends BusinessException {

    public DuplicateRoomException(String message) {
        super(message, ErrorCode.DUPLICATE_ROOM);
    }
}
