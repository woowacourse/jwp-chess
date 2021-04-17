package chess.service;

import org.springframework.stereotype.Service;

import chess.domain.board.BoardDto;
import chess.domain.chess.Chess;
import chess.domain.chess.ChessDao;
import chess.domain.chess.ChessDto;
import chess.domain.piece.PieceDao;
import chess.domain.position.MovePosition;

@Service
public class ChessService {

    private final ChessDao chessDao;
    private final PieceDao pieceDao;

    public ChessService(ChessDao chessDao, PieceDao pieceDao) {
        this.chessDao = chessDao;
        this.pieceDao = pieceDao;
    }

    public ChessDto getChessGame(Long chessId) {
        final Chess chess = chessDao.findChessById(chessId);
        return new ChessDto(chess);
    }

    public Long insert() {
        final Chess chess = Chess.createWithEmptyBoard()
                                 .start();
        final BoardDto boardDto = BoardDto.from(chess);

        final long chessId = chessDao.insert();
        pieceDao.insert(chessId, boardDto);

        return chessId;
    }

    public void move(Long chessId, MovePosition movePosition) {
        final Chess chess = chessDao.findChessById(chessId)
                                    .move(movePosition);
        pieceDao.move(chessId, movePosition);
        chessDao.updateChess(chessId, chess.status(), chess.color());
    }
}
