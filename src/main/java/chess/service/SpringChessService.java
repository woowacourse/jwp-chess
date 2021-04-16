package chess.service;

import chess.dao.ChessDao;
import chess.dao.MovementDao;
import chess.domain.board.Board;
import chess.service.dto.TilesDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SpringChessService {

    private final ChessDao chessDao;
    private final MovementDao movementDao;

    public SpringChessService(final ChessDao chessDao, final MovementDao movementDao) {
        this.chessDao = chessDao;
        this.movementDao = movementDao;
    }

    public TilesDto emptyBoard() {
        return new TilesDto(Board.emptyBoard());
    }
}
