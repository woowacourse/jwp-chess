package chess.exception.domain;

import chess.exception.BusinessException;
import chess.exception.ErrorCode;

public class InvalidPieceMoveException extends BusinessException {

    public InvalidPieceMoveException(String message) {
        super(message, ErrorCode.INVALID_PIECE_MOVE);
    }
}
