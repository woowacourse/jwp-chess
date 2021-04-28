package chess.exception.domain;

import chess.exception.BusinessException;
import chess.exception.ErrorCode;

public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND);
    }
}
